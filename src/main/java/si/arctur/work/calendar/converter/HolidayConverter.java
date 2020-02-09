package si.arctur.work.calendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.model.HolidayDTO;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HolidayConverter implements Converter<HolidayEntity, HolidayDTO> {

    @Autowired
    private WorkCalendarConverter workCalendarConverter;

    @Override
    public HolidayDTO convert(HolidayEntity holidayEntity) {
        HolidayDTO holidayDTO = null;

        if(Objects.nonNull(holidayEntity)) {
            holidayDTO = new HolidayDTO();
            holidayDTO.setDate(holidayEntity.getDate());
            holidayDTO.setId(holidayEntity.getId());
            holidayDTO.setName(holidayEntity.getName());
            holidayDTO.setWorkFree(holidayEntity.getWorkFree());

            holidayDTO.getWorkCalendars().addAll(holidayEntity.getWorkCalendars().stream().map(c -> workCalendarConverter.convert(c)).collect(Collectors.toList()));
        }
        return holidayDTO;
    }
}
