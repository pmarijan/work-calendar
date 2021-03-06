package si.arctur.work.calendar.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;

public interface CalendarRepository extends JpaRepository<WorkCalendarEntity, Long> {

    WorkCalendarEntity getWorkCalendarEntityById(@Param("id") Long id);
}
