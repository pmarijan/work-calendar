package si.arctur.work.calendar.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;

import java.util.Collection;

public interface CalendarRepository extends JpaRepository<WorkCalendarEntity, Long> {

    WorkCalendarEntity getWorkCalendarEntityById(@Param("id") Long id);

//    Collection<WorkCalendarEntity> getWorkCalendarEntitiesByDescriptionOrNameOrWorkdaysOrYear(@Param("description") String description, @Param("name") String name, @Param("workDays") String workDays, @Param("year") Integer year);
}
