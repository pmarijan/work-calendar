package si.arctur.work.calendar.service;

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

        Assert.isNull(calendarId, "calendarId attribute must not be null!");

        return holidayRepository.getHolidayEntitiesByWorkCalendars(new WorkCalendarEntity(calendarId)).stream()
                .map(holiday -> holidayConverter.convert(holiday))
                .collect(Collectors.toList());
    }

    public HolidayDTO getHoliday(Long id) {
        LOG.info("START - getHoliday(id={})", id);

        Assert.isNull(id, "id attribute must not be null!");

        return holidayConverter.convert(holidayRepository.getHolidayEntityById(id));
    }

    public HolidayDTO addHoliday(HolidayDTO holidayDTO) {
        LOG.info("START - addHoliday(holidayDTO={})", holidayDTO);

        Assert.isNull(holidayDTO, "HolidayDTO object must not be null!");

        HolidayEntity holidayEntity = new HolidayEntity();
        holidayEntity.setDate(holidayDTO.getDate());
        holidayEntity.setName(holidayDTO.getName());
        holidayEntity.setWorkFree(holidayDTO.getWorkFree());

        //TODO: check if all ok
        holidayEntity.getWorkCalendars().addAll(holidayDTO.getWorkCalendars().stream()
                .map(workCalendarDTO -> new WorkCalendarEntity(workCalendarDTO.getId()))
                .collect(Collectors.toList()));

        return holidayConverter.convert(holidayRepository.save(holidayEntity));
    }

    public HolidayDTO updateHoliday(HolidayDTO holidayDTO) {
        LOG.info("START - updateHoliday(holidayDTO={})", holidayDTO);

        Assert.isNull(holidayDTO, "HolidayDTO object must not be null!");

        HolidayEntity holidayEntity = holidayRepository.getHolidayEntityById(holidayDTO.getId());

        holidayEntity.setWorkFree(holidayDTO.getWorkFree());
        holidayEntity.setName(holidayDTO.getName());
        holidayEntity.setDate(holidayDTO.getDate());

        holidayEntity.setWorkCalendars(holidayDTO.getWorkCalendars().stream()
                .map(workCalendarDTO -> new WorkCalendarEntity(workCalendarDTO.getId()))
                .collect(Collectors.toSet()));

        return holidayConverter.convert(holidayRepository.save(holidayEntity));
    }

    public void deleteHoliday(Long id) {
        LOG.info("START - deleteHoliday(id={})", id);

        Assert.isNull(id, "id attribute must not be null!");

        holidayRepository.delete(new HolidayEntity(id));
    }
}
