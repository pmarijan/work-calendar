package si.arctur.work.calendar.service;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import si.arctur.work.calendar.converter.HolidayConverter;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.repository.CalendarRepository;
import si.arctur.work.calendar.dao.repository.HolidayRepository;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.HolidayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HolidayService {
    private static final Logger LOG = LoggerFactory.getLogger(HolidayService.class);

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private HolidayConverter holidayConverter;

    public List<HolidayDTO> getHolidays(LocalDate date, String name, Boolean isWorkFree) {
        LOG.info("START - getHolidays(date={}, name={}, isWorkFree={})", date, name, isWorkFree);

        HolidayEntity holidayEntity = new HolidayEntity();
        holidayEntity.setDate(date);
        holidayEntity.setName(name);
        holidayEntity.setWorkFree(isWorkFree);

        return holidayRepository.findAll(Example.of(holidayEntity)).stream()
                .map(holiday -> holidayConverter.convert(holiday))
                .collect(Collectors.toList());
    }

    public List<HolidayDTO> getHolidaysForCalendar(Long calendarId) {
        LOG.info("START - getHolidaysForCalendar(calendarId={})", calendarId);

        if(Objects.isNull(calendarId)) {
            LOG.error("calendarId attribute must not be null!");
            throw new IllegalArgumentException("calendarId attribute must not be null!");
        }

        return holidayRepository.getHolidayEntitiesByWorkCalendars(new WorkCalendarEntity(calendarId)).stream()
                .map(holiday -> holidayConverter.convert(holiday))
                .collect(Collectors.toList());
    }

    public HolidayDTO getHoliday(Long id) {
        LOG.info("START - getHoliday(id={})", id);

        if(Objects.isNull(id)) {
            LOG.error("id attribute must not be null!");
            throw new IllegalArgumentException("id attribute must not be null!");
        }

        return holidayConverter.convert(holidayRepository.getHolidayEntityById(id));
    }

    public HolidayDTO addHolidayToCalendar(Long calendarId, HolidayDTO holidayDTO) {
        LOG.info("START - addHoliday(calendarId={}, holidayDTO={})", calendarId, holidayDTO);

        //check if calendar exists
        WorkCalendarEntity workCalendarEntity = calendarRepository.getWorkCalendarEntityById(calendarId);
        if(Objects.isNull(workCalendarEntity)) {
            LOG.error("workcalendar object with id={} does not exist!", calendarId);
            throw new ResourceNotFoundException("workcalendar object does not exist!");
        }

        //check if holiday for selected date and name exists
        HolidayEntity holidayEntity = holidayRepository.getHolidayEntityByDateAndName(holidayDTO.getDate(), holidayDTO.getName());

        //if doesn't exist create new one
        if(Objects.isNull(holidayEntity)) {
            holidayEntity = new HolidayEntity();
            holidayEntity.setDate(holidayDTO.getDate());
            holidayEntity.setName(holidayDTO.getName());
            holidayEntity.setWorkFree(holidayDTO.getWorkFree());
            holidayRepository.save(holidayEntity);
        }

        workCalendarEntity.getHolidays().add(holidayEntity);
        calendarRepository.save(workCalendarEntity);

        return holidayConverter.convert(holidayEntity);
    }

    /**
     * Add new holiday without adding reference to work calendar
     * @param holidayDTO
     * @return
     */
    public HolidayDTO addHoliday(HolidayDTO holidayDTO) {
        LOG.info("START - addHoliday(holidayDTO={})", holidayDTO);

        if(Objects.isNull(holidayDTO)) {
            LOG.error("holidayDTO attribute must not be null!");
            throw new IllegalArgumentException("holidayDTO attribute must not be null!");
        }

        HolidayEntity holidayEntity = new HolidayEntity();
        holidayEntity.setDate(holidayDTO.getDate());
        holidayEntity.setName(holidayDTO.getName());
        holidayEntity.setWorkFree(holidayDTO.getWorkFree());

        return holidayConverter.convert(holidayRepository.save(holidayEntity));
    }

    /**
     * Update selected holiday, without changing reference to work calendar
     * @param holidayDTO
     * @return
     */
    public HolidayDTO updateHoliday(HolidayDTO holidayDTO) {
        LOG.info("START - updateHoliday(holidayDTO={})", holidayDTO);

        if(Objects.isNull(holidayDTO)) {
            LOG.error("HolidayDTO attribute must not be null!");
            throw new IllegalArgumentException("HolidayDTO attribute must not be null!");
        }

        HolidayEntity holidayEntity = holidayRepository.getHolidayEntityById(holidayDTO.getId());

        holidayEntity.setWorkFree(holidayDTO.getWorkFree());
        holidayEntity.setName(holidayDTO.getName());
        holidayEntity.setDate(holidayDTO.getDate());

        return holidayConverter.convert(holidayRepository.save(holidayEntity));
    }

    /**
     * Check if provided calendarId matches with one from DB
     * @param calendarId
     * @param holidayId
     */
    @Transactional
    public void deleteHoliday(Long calendarId, Long holidayId) {
        HolidayEntity holidayEntity = holidayRepository.getHolidayEntityById(holidayId);
        int numOfCalendarReferences = holidayEntity.getWorkCalendars().size();
        LOG.info("numOfCalendarReferences={}", numOfCalendarReferences);

        holidayEntity.getWorkCalendars().stream()
                .filter(c -> c.getId().equals(calendarId))
                .findAny()
                .orElseThrow(() -> {throw new IllegalArgumentException("Workcalendar id mismatch!");});

        //delete mapping if exists
        holidayRepository.deleteHolidayToWorkCalendarMapping(calendarId, holidayId);

        //if there is no more references to work calendar, delete holiday
        if(numOfCalendarReferences == 1) {
            deleteHoliday(holidayId);
        }
    }

    /**
     * Delete holiday record only if it does not have mapping to work calendar
     * @param id
     */
    @Transactional
    public void deleteHoliday(Long id) {
        LOG.info("START - deleteHoliday(id={})", id);

        if(Objects.isNull(id)) {
            LOG.error("id attribute must not be null!");
            throw new IllegalArgumentException("id attribute must not be null!");
        }

        holidayRepository.delete(new HolidayEntity(id));
    }
}
