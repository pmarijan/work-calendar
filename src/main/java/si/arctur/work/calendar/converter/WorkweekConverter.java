package si.arctur.work.calendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.dao.entity.WorkweekEntity;
import si.arctur.work.calendar.model.WorkweekDTO;

import java.util.Objects;

@Component
public class WorkweekConverter implements Converter<WorkweekEntity, WorkweekDTO> {

    @Autowired
    private WorkCalendarConverter workCalendarConverter;

    @Override
    public WorkweekDTO convert(WorkweekEntity workweekEntity) {
        WorkweekDTO workweekDTO = null;

        if(Objects.nonNull(workweekEntity)) {
            workweekDTO = new WorkweekDTO();
            workweekDTO.setDescription(workweekEntity.getDescription());
            workweekDTO.setId(workweekEntity.getId());
            workweekDTO.setWeekNumber(workweekEntity.getWeekNumber());
            workweekDTO.setWorkCalendar(workCalendarConverter.convert(workweekEntity.getWorkCalendar()));
        }

        return workweekDTO;
    }
}
