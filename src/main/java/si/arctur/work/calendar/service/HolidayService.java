package si.arctur.work.calendar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.converter.HolidayConverter;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.repository.CalendarRepository;
import si.arctur.work.calendar.dao.repository.HolidayRepository;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.HolidayDTO;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
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

    /**
     *
     * @param calendarId
     * @param date
     * @param name
     * @param isWorkFree
     * @return
     */
    public List<HolidayDTO> getHolidays(Long calendarId, LocalDate date, String name, Boolean isWorkFree) {
        LOG.info("START - getHolidays(calendarId={}, date={}, name={}, isWorkFree={})", calendarId, date, name, isWorkFree);

        return holidayRepository.getHolidayEntities(calendarId, date, name, isWorkFree).stream()
                .map(holiday -> holidayConverter.convert(holiday))
                .collect(Collectors.toList());
    }

    public HolidayDTO getHoliday(Long calendarId, Long id) {
        LOG.info("START - getHoliday(calendarId={}, id={})", calendarId, id);

        if(Objects.isNull(id) || Objects.isNull(calendarId)) {
            LOG.error("id and calenadrId attribute must not be null!");
            throw new IllegalArgumentException("id and calenadrId attribute must not be null!");
        }

        return holidayConverter.convert(holidayRepository.getHolidayEntityByIdAndWorkCalendarId(id, calendarId));
    }

    @Transactional
    public HolidayDTO addHolidayToCalendar(Long calendarId, HolidayDTO holidayDTO) {
        LOG.info("START - addHoliday(calendarId={}, holidayDTO={})", calendarId, holidayDTO);

        if(Objects.isNull(holidayDTO)) {
            LOG.error("holidayDTO object is null");
            throw new IllegalArgumentException("holidayDTO object is null");
        }

        //check if calendar exists
        WorkCalendarEntity workCalendarEntity = calendarRepository.getWorkCalendarEntityById(calendarId);
        if(Objects.isNull(workCalendarEntity)) {
            LOG.error("workcalendar object with id={} does not exist!", calendarId);
            throw new ResourceNotFoundException("workcalendar object does not exist!");
        }

        if(!workCalendarEntity.getYear().equals(holidayDTO.getDate().getYear())) {
            LOG.error("Calendar year={} and holiday year={} do not match", workCalendarEntity.getYear(), holidayDTO.getDate().getYear());
            throw new IllegalArgumentException("Calendar year and holiday year do not match");
        }

        //check if holiday for selected date and name exists
        HolidayEntity holidayEntity = holidayRepository.getHolidayEntityByDateAndName(holidayDTO.getDate(), holidayDTO.getName());

        //if doesn't exist create new one
        if(Objects.isNull(holidayEntity)) {
            holidayEntity = new HolidayEntity();
            holidayEntity.setDate(holidayDTO.getDate());
            holidayEntity.setName(holidayDTO.getName());
            holidayEntity.setWorkFree(holidayDTO.getWorkFree());
            holidayEntity = holidayRepository.save(holidayEntity);
        }
        LOG.info("holidayEntity.id=" + holidayEntity.getId());

        workCalendarEntity.getHolidays().add(holidayEntity);
        calendarRepository.save(workCalendarEntity);

        return holidayConverter.convert(holidayEntity);
    }

    /**
     * Update selected holiday, without changing reference to work calendar
     * @param holidayDTO
     * @return
     */
    @Transactional
    public HolidayDTO updateHoliday(Long calendarId, HolidayDTO holidayDTO) {
        LOG.info("START - updateHoliday(calendarId={}, holidayDTO={})", calendarId, holidayDTO);

        if(Objects.isNull(holidayDTO) || Objects.isNull(calendarId)) {
            LOG.error("HolidayDTO or calendarId attribute must not be null!");
            throw new IllegalArgumentException("HolidayDTO or calendarId attribute must not be null!");
        }

        HolidayEntity holidayEntity = holidayRepository.getHolidayEntityByIdAndWorkCalendarId(holidayDTO.getId(), calendarId);

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
        HolidayEntity holidayEntity = holidayRepository.getHolidayEntityByIdAndWorkCalendarId(holidayId, calendarId);
        int numOfCalendarReferences = holidayEntity.getWorkCalendars().size();
        LOG.info("numOfCalendarReferences={}", numOfCalendarReferences);

        if(holidayEntity.getWorkCalendars().stream()
                .filter(c -> c.getId().equals(calendarId))
                .findAny().isPresent()) {
            throw new IllegalArgumentException("Workcalendar id mismatch!");
        }

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