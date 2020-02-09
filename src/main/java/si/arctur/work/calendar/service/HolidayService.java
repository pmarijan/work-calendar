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
import si.arctur.work.calendar.dao.repository.HolidayRepository;
import si.arctur.work.calendar.model.HolidayDTO;
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

        //add holiday to all selected calendars
//        holidayEntity.getWorkCalendars().addAll(holidayDTO.getWorkCalendars().stream()
//                .map(workCalendarDTO -> new WorkCalendarEntity(workCalendarDTO.getId()))
//                .collect(Collectors.toList()));

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
//        Assert.notNull(holidayDTO, "HolidayDTO object must not be null!");

        HolidayEntity holidayEntity = holidayRepository.getHolidayEntityById(holidayDTO.getId());

        holidayEntity.setWorkFree(holidayDTO.getWorkFree());
        holidayEntity.setName(holidayDTO.getName());
        holidayEntity.setDate(holidayDTO.getDate());

//        holidayEntity.setWorkCalendars(holidayDTO.getWorkCalendars().stream()
//                .map(workCalendarDTO -> new WorkCalendarEntity(workCalendarDTO.getId()))
//                .collect(Collectors.toList()));

        return holidayConverter.convert(holidayRepository.save(holidayEntity));
    }

    public void deleteHoliday(Long id) {
        LOG.info("START - deleteHoliday(id={})", id);

        if(Objects.isNull(id)) {
            LOG.error("id attribute must not be null!");
            throw new IllegalArgumentException("id attribute must not be null!");
        }

        holidayRepository.delete(new HolidayEntity(id));
    }
}
