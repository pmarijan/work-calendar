package si.arctur.work.calendar.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import java.time.LocalDate;
import java.util.Collection;

public interface HolidayRepository extends JpaRepository<HolidayEntity, Long> {

    HolidayEntity getHolidayEntityById(@Param("id") Long id);

    Collection<HolidayEntity> getHolidayEntitiesByDateOrNameOrWorkFree(@Param("date") LocalDate date, @Param("name") String name, @Param("workFree") Boolean workFree);

//    @Query("SELECT h FROM HolidayEntity h WHERE h.workCalendars.id = :calendarId")
    Collection<HolidayEntity> getHolidayEntitiesByWorkCalendars(@Param("calendarId") WorkCalendarEntity workCalendarEntity);

//    public Collection<HolidayEntity> findByWorkCalendars_id
}
