package si.arctur.work.calendar.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.model.WorkCalendarDTO;

@Component
public class WorkCalendarConverter implements Converter<WorkCalendarEntity, WorkCalendarDTO> {
    @Override
    public WorkCalendarDTO convert(WorkCalendarEntity workCalendarEntity) {
        WorkCalendarDTO workCalendarDTO = new WorkCalendarDTO();

        workCalendarDTO.setId(workCalendarEntity.getId());
        workCalendarDTO.setDescription(workCalendarEntity.getDescription());
        workCalendarDTO.setName(workCalendarEntity.getName());
        workCalendarDTO.setWorkdays(workCalendarEntity.getWorkdays());
        workCalendarDTO.setYear(workCalendarEntity.getYear());

        return workCalendarDTO;
    }
}
