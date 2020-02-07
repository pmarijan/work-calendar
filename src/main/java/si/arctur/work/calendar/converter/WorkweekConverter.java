package si.arctur.work.calendar.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.dao.entity.WorkweekEntity;
import si.arctur.work.calendar.model.WorkweekDTO;

@Component
public class WorkweekConverter implements Converter<WorkweekEntity, WorkweekDTO> {
    @Override
    public WorkweekDTO convert(WorkweekEntity workweekEntity) {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        workweekDTO.setDescription(workweekEntity.getDescription());
        workweekDTO.setId(workweekEntity.getId());
        workweekDTO.setWeekNumber(workweekEntity.getWeekNumber());
//        workweekDTO.setWorkCalendar(workweekEntity.getWorkCalendar());

        return workweekDTO;
    }
}
