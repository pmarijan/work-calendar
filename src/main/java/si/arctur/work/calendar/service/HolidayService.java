package si.arctur.work.calendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
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

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private HolidayConverter holidayConverter;

    public List<HolidayDTO> getHolidays(LocalDate date, String name, boolean isWorkFree) {
        HolidayEntity holidayEntity = new HolidayEntity();
        holidayEntity.setDate(date);
        holidayEntity.setName(name);
        holidayEntity.setWorkFree(isWorkFree);

        return holidayRepository.findAll(Example.of(holidayEntity)).stream().map(holiday -> holidayConverter.convert(holiday)).collect(Collectors.toList());
    }

    public List<HolidayDTO> getHolidaysForCalendar(Long calendarId) {
        return holidayRepository.getHolidayEntitiesByWorkCalendars(new WorkCalendarEntity(calendarId)).stream().map(holiday -> holidayConverter.convert(holiday)).collect(Collectors.toList());
    }

    public HolidayDTO getHoliday(Long id) {
        return holidayConverter.convert(holidayRepository.getHolidayEntityById(id));
    }

    public HolidayDTO addHoliday(HolidayDTO holidayDTO) {
        HolidayEntity holidayEntity = new HolidayEntity();
        holidayEntity.setDate(holidayDTO.getDate());
        holidayEntity.setName(holidayDTO.getName());
        holidayEntity.setWorkFree(holidayDTO.getWorkFree());
        holidayEntity.getWorkCalendars().addAll(holidayDTO.getWorkCalendars().stream().map(workCalendarDTO -> new WorkCalendarEntity(workCalendarDTO.getId())).collect(Collectors.toList()));

        return holidayConverter.convert(holidayRepository.save(holidayEntity));
    }

    public HolidayDTO updateHoliday(HolidayDTO holidayDTO) {
        HolidayEntity holidayEntity = holidayRepository.getHolidayEntityById(holidayDTO.getId());

        holidayEntity.setWorkFree(holidayDTO.getWorkFree());
        holidayEntity.setName(holidayDTO.getName());
        holidayEntity.setDate(holidayDTO.getDate());
        holidayEntity.setWorkCalendars(holidayDTO.getWorkCalendars().stream().map(workCalendarDTO -> new WorkCalendarEntity(workCalendarDTO.getId())).collect(Collectors.toSet()));

        return holidayConverter.convert(holidayRepository.save(holidayEntity));
    }

    public void deleteHoliday(Long id) {
        holidayRepository.delete(new HolidayEntity(id));
    }
}
