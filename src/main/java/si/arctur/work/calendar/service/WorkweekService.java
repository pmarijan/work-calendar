package si.arctur.work.calendar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.converter.WorkweekConverter;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.entity.WorkweekEntity;
import si.arctur.work.calendar.dao.repository.WorkweekRepository;
import si.arctur.work.calendar.model.WorkweekDTO;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkweekService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkweekService.class);

    @Autowired
    private WorkweekRepository workweekRepository;

    @Autowired
    private WorkweekConverter workweekConverter;

    public List<WorkweekDTO> getWorkweeks(String description, Integer weekNumber) {
        LOG.info("START - getWorkWeeks(description={}, weekNumber={})", description, weekNumber);

        return workweekRepository.getWorkweekEntitiesByDescriptionOrWeekNumber(description, weekNumber).stream().map(workWeek -> workweekConverter.convert(workWeek)).collect(Collectors.toList());
    }

    public WorkweekDTO getWorkweek(Long workweekId) {
        LOG.info("START - getWorkweek(workweekId={})", workweekId);

        return workweekConverter.convert(workweekRepository.getWorkweekEntityById(workweekId));
    }

    public List<WorkweekDTO> getWorkweeksByCalendarId(Long calendarId) {
        LOG.info("START - getWorkweeksByCalendarId(calendarId={})", calendarId);

        return workweekRepository.getWorkweekEntitiesByWorkCalendar(calendarId).stream().map(workWeek -> workweekConverter.convert(workWeek)).collect(Collectors.toList());
    }

    public WorkweekDTO addWorkweek(WorkweekDTO workweekDTO) {
        LOG.info("START - addWorkweek(workweekDTO={})", workweekDTO);

        WorkweekEntity workweekEntity = new WorkweekEntity();
        workweekEntity.setDescription(workweekDTO.getDescription());
        workweekEntity.setWeekNumber(workweekDTO.getWeekNumber());
        workweekEntity.setWorkCalendar(new WorkCalendarEntity(workweekDTO.getWorkCalendar().getId()));

        return workweekConverter.convert(workweekRepository.save(workweekEntity));
    }

    public WorkweekDTO updateWorkweek(WorkweekDTO workweekDTO) {
        LOG.info("START - updateWorkweek(workweekDTO={})", workweekDTO);

        WorkweekEntity workweekEntity = workweekRepository.getWorkweekEntityById(workweekDTO.getId());
        workweekEntity.setDescription(workweekDTO.getDescription());
        workweekEntity.setWeekNumber(workweekDTO.getWeekNumber());

        return workweekConverter.convert(workweekRepository.save(workweekEntity));
    }

    public void deleteWorkweek(long workweekId) {
        LOG.info("START - deleteWorkweek(workweekId={})", workweekId);
        workweekRepository.delete(new WorkweekEntity(workweekId));
    }
}
