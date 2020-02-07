package si.arctur.work.calendar.dao.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import si.arctur.work.calendar.dao.entity.WorkweekEntity;

import java.util.Collection;

public interface WorkweekRepository extends CrudRepository<WorkweekEntity, Long> {

    Collection<WorkweekEntity> getWorkweekEntitiesByDescriptionOrWeekNumber(@Param("description") String description, @Param("weekNumber") Integer weekNumber);

    WorkweekEntity getWorkweekEntityById(@Param("id") Long id);

    @Query(value = "SELECT ww FROM WorkweekEntity ww WHERE ww.workCalendar.id = :workCalendarId")
    Collection<WorkweekEntity> getWorkweekEntitiesByWorkCalendar(@Param("workCalendarId") Long workCalendarId);
}
