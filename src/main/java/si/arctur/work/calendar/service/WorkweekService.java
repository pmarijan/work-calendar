package si.arctur.work.calendar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.converter.WorkweekConverter;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.entity.WorkweekEntity;
import si.arctur.work.calendar.dao.repository.CalendarRepository;
import si.arctur.work.calendar.dao.repository.WorkweekRepository;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.WorkweekDTO;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class WorkweekService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkweekService.class);

    @Autowired
    private WorkweekRepository workweekRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private WorkweekConverter workweekConverter;

    public List<WorkweekDTO> getWorkweeks(Long calendarId, String description, Integer weekNumber) {
        LOG.info("START - getWorkWeeks(calendarId={}, description={}, weekNumber={})", calendarId, description, weekNumber);

        WorkweekEntity workweekEntity = new WorkweekEntity();
        workweekEntity.setDescription(description);
        workweekEntity.setWeekNumber(weekNumber);
        if(Objects.nonNull(calendarId)) {
            workweekEntity.setWorkCalendar(new WorkCalendarEntity(calendarId));
        }
        return workweekRepository.findAll(Example.of(workweekEntity)).stream().map(workWeek -> workweekConverter.convert(workWeek)).collect(Collectors.toList());
    }

    public WorkweekDTO getWorkweek(Long calendarId, Long workweekId) {
        LOG.info("START - getWorkweek(calendarId={}, workweekId={})", calendarId, workweekId);

        return workweekConverter.convert(workweekRepository.getWorkweekEntityByIdAndWorkCalendar(workweekId, new WorkCalendarEntity(calendarId)));
    }

    public List<WorkweekDTO> getWorkweeksByCalendarId(Long calendarId) {
        LOG.info("START - getWorkweeksByCalendarId(calendarId={})", calendarId);

        return workweekRepository.getWorkweekEntitiesByWorkCalendar(new WorkCalendarEntity(calendarId)).stream()
                .map(workWeek -> workweekConverter.convert(workWeek))
                .collect(Collectors.toList());
    }

    public WorkweekDTO addWorkweek(Long calendarId, WorkweekDTO workweekDTO) {
        LOG.info("START - addWorkweek(calendarId={}, workweekDTO={})", calendarId, workweekDTO);

        WorkCalendarEntity workCalendarEntity = calendarRepository.getWorkCalendarEntityById(calendarId);
        if(Objects.isNull(workCalendarEntity)) {
            LOG.error("workcalendar object with id={} does not exist!", calendarId);
            throw new ResourceNotFoundException("workcalendar object does not exist!");
        }

        WorkweekEntity workweekEntity = new WorkweekEntity();
        workweekEntity.setDescription(workweekDTO.getDescription());
        workweekEntity.setWeekNumber(workweekDTO.getWeekNumber());
        workweekEntity.setWorkCalendar(workCalendarEntity);

        return workweekConverter.convert(workweekRepository.save(workweekEntity));
    }

    public WorkweekDTO updateWorkweek(Long calendarId, WorkweekDTO workweekDTO) {
        LOG.info("START - updateWorkweek(calendarId={}, workweekDTO={})", calendarId, workweekDTO);

        WorkweekEntity workweekEntity = workweekRepository.getWorkweekEntityByIdAndWorkCalendar(workweekDTO.getId(), new WorkCalendarEntity(calendarId));
        if(Objects.isNull(workweekEntity)) {
            LOG.error("workweek object with calendarId={} and id={} does not exist!", calendarId, workweekDTO.getId());
            throw new ResourceNotFoundException("workcalendar object does not exist!");
        }

        workweekEntity.setDescription(workweekDTO.getDescription());
        workweekEntity.setWeekNumber(workweekDTO.getWeekNumber());

        return workweekConverter.convert(workweekRepository.save(workweekEntity));
    }

    public void deleteWorkweek(Long calendarId, Long workweekId) {
        LOG.info("START - deleteWorkweek(calendarId={}, workweekId={})", calendarId, workweekId);

        WorkCalendarEntity workCalendarEntity = calendarRepository.getWorkCalendarEntityById(calendarId);
        if(Objects.isNull(workCalendarEntity)) {
            LOG.error("workcalendar object with id={} does not exist!", calendarId);
            throw new ResourceNotFoundException("workcalendar object does not exist!");
        }

        WorkweekEntity workweekEntity = workweekRepository.getWorkweekEntityByIdAndWorkCalendar(workweekId, workCalendarEntity);
        if(Objects.isNull(workweekEntity)) {
            LOG.error("workweek object with id={} does not exist!", workweekId);
            throw new ResourceNotFoundException("workweek object does not exist!");
        }

        workweekRepository.delete(workweekEntity);

        LOG.info("END - deleteWorkweek");
    }
}
