package si.arctur.work.calendar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.converter.DayOfWeekEnumSetConverter;
import si.arctur.work.calendar.converter.HolidayConverter;
import si.arctur.work.calendar.converter.WorkCalendarConverter;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.repository.CalendarRepository;
import si.arctur.work.calendar.dao.repository.HolidayRepository;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.DayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
public class WorkCalendarService {
	private static final Logger LOG = LoggerFactory.getLogger(WorkCalendarService.class);

	@Autowired
	private CalendarRepository calendarRepository;

	@Autowired
	private HolidayRepository holidayRepository;

	@Autowired
	private WorkCalendarConverter workCalendarConverter;

	@Autowired
	private HolidayConverter holidayConverter;

	@Autowired
	private DayOfWeekEnumSetConverter dayOfWeekEnumSetConverter;

	public List<WorkCalendarDTO> getWorkCalendars(String description, String name, EnumSet<DayOfWeek> workDays, Integer year) {
		LOG.info("START - getWorkCalendars(description={}, name={}, workDays={}, year={})", description, name, workDays, year);

		WorkCalendarEntity workCalendarEntity = new WorkCalendarEntity();
		workCalendarEntity.setDescription(description);
		workCalendarEntity.setName(name);
		if(Objects.nonNull(workDays)) {
			workCalendarEntity.setWorkdays(workDays.stream().map(d -> d.name()).collect(Collectors.joining(",")));
		}
		workCalendarEntity.setYear(year);

		List<WorkCalendarEntity> workCalendarEntities = calendarRepository.findAll(Example.of(workCalendarEntity));

		LOG.info("END - getWorkCalendars");
		return workCalendarEntities.stream().map(workCalendarConverter::convert).collect(Collectors.toList());
	}

	public WorkCalendarDTO getWorkCalendar(Long id) {
		return workCalendarConverter.convert(calendarRepository.getWorkCalendarEntityById(id));
	}

	public long getWorkdayCount(Long calendarId, LocalDate from, LocalDate to) {
        LOG.info("START - getWorkdayCount(calendarId={}, from={}, to={})", calendarId, from, to);

        //get collection of holidays for year from provided date range
		WorkCalendarEntity workCalendarEntity = calendarRepository.getWorkCalendarEntityById(calendarId);
		if(Objects.isNull(workCalendarEntity)) {
			LOG.error("workcalendar object with calendarId={} does not exist!", calendarId);
			throw new ResourceNotFoundException("workcalendar object does not exist!");
		}

		if(workCalendarEntity.getYear() != from.getYear()) {
			LOG.error("workcalendar year={} and selected interval year={} do not match!", workCalendarEntity.getYear(), from.getYear());
			throw new IllegalArgumentException("workcalendar year=" +workCalendarEntity.getYear()+" and selected interval year=" + from.getYear() + " do not match");
		}

		Set<HolidayEntity> holidays = workCalendarEntity.getHolidays();
		LOG.info("number of holidays for calendar={}", holidays.size());

        //convert collection to map for easier date search
        Map<LocalDate, HolidayEntity> holidaysMap = holidays.stream().collect(Collectors.toMap(h -> h.getDate(), h -> h));

        //function check if date is weekend or holiday
        Function<LocalDate, Boolean> isWorkday = (LocalDate date) -> {
            Boolean result = false;

            //convert comma delimited string to set of workdays from work calendar
			Set<DayOfWeek> workdays = dayOfWeekEnumSetConverter.convert(workCalendarEntity.getWorkdays());

            //day is working day if it's not weekend and if it's not a workfree holiday
            if(workdays.contains(date.getDayOfWeek())) {

				//check if it's holiday and if it's workfree holiday
            	result = !(holidaysMap.containsKey(date) && holidaysMap.get(date).getWorkFree());
            }

            return result;
        };

        long numOfWorkingDays = getDatesRange(from, to).stream()
				.filter(d -> isWorkday.apply(d)).count();

        LOG.info("END - getWorkdayCount: {}", numOfWorkingDays);
        return numOfWorkingDays;
    }

	public List<DayDTO> getListOfDays(Long calendarId, LocalDate from, LocalDate to) {
		LOG.info("START - getListOfDays(calendarId={}, from={}, to={})", calendarId, from, to);

		//get collection of holidays for year from provided date range
		WorkCalendarEntity workCalendarEntity = calendarRepository.getWorkCalendarEntityById(calendarId);
		if(Objects.isNull(workCalendarEntity)) {
			LOG.error("workcalendar object with calendarId={} does not exist!", calendarId);
			throw new ResourceNotFoundException("workcalendar object does not exist!");
		}

		Set<HolidayEntity> holidays = workCalendarEntity.getHolidays();

		//convert collection to map for easier date search
		Map<LocalDate, HolidayEntity> holidaysMap = holidays.stream().collect(Collectors.toMap(h -> h.getDate(), h -> h));

		//function check if date is weekend or holiday
		Function<LocalDate, DayDTO> workdayOrHolidayCheck = (LocalDate date) -> {
			DayDTO day = new DayDTO();
			day.setDate(date);
			day.setDayOfWeek(date.getDayOfWeek().name());

			Set<DayOfWeek> workdays = dayOfWeekEnumSetConverter.convert(workCalendarEntity.getWorkdays());
			//check if date is weekend or not, which day is weekend or workday is specified in WorkCalendar.workdays
			day.setWeekend(!workdays.contains(date.getDayOfWeek()));

			if(holidaysMap.containsKey(date)) {
				day.setHoliday(holidayConverter.convert(holidaysMap.get(date)));
			}

			return day;
		};

		//iterate trough list of dates and check if weekend or holiday
		List<DayDTO> days = getDatesRange(from, to).stream().map(l -> workdayOrHolidayCheck.apply(l)).collect(Collectors.toList());

		LOG.info("END - getListOfDays: {}", days);
		return days;
	}

	/**
	 * Returns list of dates for selected interval
	 *
	 * @param from - date from
	 * @param to - date to
	 * @return List<LocalDate>
	 */
	private List<LocalDate> getDatesRange(LocalDate from, LocalDate to) {
		if(from.isAfter(to)) {
			throw new IllegalArgumentException("From date can not be after to date!");
		}
		long numberOfDays = ChronoUnit.DAYS.between(from, to) + 1;

		return LongStream.range(0, numberOfDays).mapToObj(from::plusDays).collect(Collectors.toList());
	}
}