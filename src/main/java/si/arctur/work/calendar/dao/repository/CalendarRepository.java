package si.arctur.work.calendar.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;

import java.util.Collection;

public interface CalendarRepository extends CrudRepository<WorkCalendarEntity, Long> {

    @Query(value = "SELECT wc FROM WorkCalendarEntity wc")
    Collection<WorkCalendarEntity> getAllWorkCalendars();
}
