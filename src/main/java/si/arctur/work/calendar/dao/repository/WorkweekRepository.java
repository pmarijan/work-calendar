package si.arctur.work.calendar.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.entity.WorkweekEntity;
import java.util.Collection;

public interface WorkweekRepository extends JpaRepository<WorkweekEntity, Long> {

    WorkweekEntity getWorkweekEntityByIdAndWorkCalendar(Long id, WorkCalendarEntity workCalendarEntity);

    Collection<WorkweekEntity> getWorkweekEntitiesByWorkCalendar(WorkCalendarEntity workCalendarEntity);
}
