package si.arctur.work.calendar.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.model.HolidayDTO;

@Component
public class HolidayConverter implements Converter<HolidayEntity, HolidayDTO> {
    @Override
    public HolidayDTO convert(HolidayEntity holidayEntity) {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setDate(holidayEntity.getDate());
        holidayDTO.setId(holidayEntity.getId());
        holidayDTO.setName(holidayEntity.getName());
        holidayDTO.setWorkFree(holidayEntity.getWorkFree());
//        holidayDTO.getWorkCalendars().addAll
        //TODO: add work calendars;
        return holidayDTO;
    }
}
