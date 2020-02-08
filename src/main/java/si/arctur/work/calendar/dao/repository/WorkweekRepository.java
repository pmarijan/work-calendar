package si.arctur.work.calendar.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import si.arctur.work.calendar.dao.entity.WorkweekEntity;
import java.util.Collection;

public interface WorkweekRepository extends JpaRepository<WorkweekEntity, Long> {

//    Collection<WorkweekEntity> getWorkweekEntitiesByDescriptionOrWeekNumber(@Param("description") String description, @Param("weekNumber") Integer weekNumber);

    WorkweekEntity getWorkweekEntityById(@Param("id") Long id);

    Collection<WorkweekEntity> getWorkweekEntitiesByWorkCalendar(@Param("workCalendarId") Long workCalendarId);
}
