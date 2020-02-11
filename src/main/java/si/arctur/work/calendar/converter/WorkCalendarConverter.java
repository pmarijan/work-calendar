package si.arctur.work.calendar.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.dao.EnumSetToStringConverter;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.model.WorkCalendarDTO;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Objects;

@Component
public class WorkCalendarConverter implements Converter<WorkCalendarEntity, WorkCalendarDTO> {

    @Autowired
    private DayOfWeekEnumSetConverter dayOfWeekEnumSetConverter;

    @Override
    public WorkCalendarDTO convert(WorkCalendarEntity workCalendarEntity) {
        WorkCalendarDTO workCalendarDTO = null;

        if(Objects.nonNull(workCalendarEntity)) {
            workCalendarDTO = new WorkCalendarDTO();
            workCalendarDTO.setId(workCalendarEntity.getId());
            workCalendarDTO.setDescription(workCalendarEntity.getDescription());
            workCalendarDTO.setName(workCalendarEntity.getName());
            workCalendarDTO.setWorkdays(dayOfWeekEnumSetConverter.convert(workCalendarEntity.getWorkdays()));
            workCalendarDTO.setYear(workCalendarEntity.getYear());
        }

        return workCalendarDTO;
    }


}
