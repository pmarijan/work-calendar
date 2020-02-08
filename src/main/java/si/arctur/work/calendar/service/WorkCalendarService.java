package si.arctur.work.calendar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.converter.HolidayConverter;
import si.arctur.work.calendar.converter.WorkCalendarConverter;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.repository.CalendarRepository;
import si.arctur.work.calendar.dao.repository.HolidayRepository;
import si.arctur.work.calendar.model.DayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

	public List<WorkCalendarDTO> getWorkCalendars(String description, String name, String workDays, Integer year) {

		WorkCalendarEntity workCalendarEntity = new WorkCalendarEntity();
		workCalendarEntity.setDescription(description);
		workCalendarEntity.setName(name);
		workCalendarEntity.setWorkdays(workDays);
		workCalendarEntity.setYear(year);

		return calendarRepository.findAll(Example.of(workCalendarEntity)).stream().map(cal -> workCalendarConverter.convert(cal)).collect(Collectors.toList());
	}

	public WorkCalendarDTO getWorkCalendar(Long id) {
		return workCalendarConverter.convert(calendarRepository.getWorkCalendarEntityById(id));
	}

	public long getWorkdayCount(LocalDate from, LocalDate to) {
        LOG.info("START - getWorkdayCount(from={}, to={})", from, to);

        //get collection of holidays for year from provided date range
        Collection<HolidayEntity> holidays = holidayRepository.getHolidayEntitiesByYear(from.getYear());

        //convert collection to map for easier date search
        Map<LocalDate, HolidayEntity> holidaysMap = holidays.stream().collect(Collectors.toMap(h -> h.getDate(), h -> h));

        //function check if date is weekend or holiday
        Function<LocalDate, Boolean> isWorkday = (LocalDate date) -> {
            Boolean result = false;

            if(!date.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                if(holidaysMap.containsKey(date)) {
                    if (!holidaysMap.get(date).getWorkFree()) {
                        result = true;
                    }
                } else {
                    result = true;
                }
            }

            return result;
        };

        long numOfWorkingDays = getDatesRange(from, to).stream().filter(l -> isWorkday.apply(l)).count();
        return numOfWorkingDays;
    }

	public List<DayDTO> getListOfDays(LocalDate from, LocalDate to) {
		LOG.info("START - getListOfDays(from={}, to={})", from, to);

		//get collection of holidays for year from provided date range
		Collection<HolidayEntity> holidays = holidayRepository.getHolidayEntitiesByYear(from.getYear());

		//convert collection to map for easier date search
		Map<LocalDate, HolidayEntity> holidaysMap = holidays.stream().collect(Collectors.toMap(h -> h.getDate(), h -> h));

		//function check if date is weekend or holiday
		Function<LocalDate, DayDTO> isWeekendOrHoliday = (LocalDate date) -> {
			DayDTO day = new DayDTO();
			day.setDate(date);

			if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				day.setWeekend(true);
			}

			if(holidaysMap.containsKey(date)) {
				day.setHoliday(holidayConverter.convert(holidaysMap.get(date)));
			}

			return day;
		};

		//iterate trough list of dates and check if weekend or holiday
		List<DayDTO> days = getDatesRange(from, to).stream().map(l -> isWeekendOrHoliday.apply(l)).collect(Collectors.toList());

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
		long numberOfDays = ChronoUnit.DAYS.between(from, to);
		return LongStream.range(0, numberOfDays).mapToObj(from::plusDays).collect(Collectors.toList());
	}
}