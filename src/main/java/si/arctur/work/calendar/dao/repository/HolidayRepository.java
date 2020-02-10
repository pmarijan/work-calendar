package si.arctur.work.calendar.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import java.time.LocalDate;
import java.util.Collection;

public interface HolidayRepository extends JpaRepository<HolidayEntity, Long> {

    HolidayEntity getHolidayEntityById(Long id);

//    Collection<HolidayEntity> getHolidayEntitiesByDateOrNameOrWorkFree(@Param("date") LocalDate date, @Param("name") String name, @Param("workFree") Boolean workFree);

    HolidayEntity getHolidayEntityByDateAndName(LocalDate date, String name);

//    @Query("SELECT h FROM HolidayEntity h WHERE h.workCalendars.id = :calendarId")
    Collection<HolidayEntity> getHolidayEntitiesByWorkCalendars(WorkCalendarEntity workCalendarEntity);

//    @Query("SELECT h FROM HolidayEntity h WHERE YEAR(h.date) = :year AND h.workCalendars = :calendar")
    @Query("SELECT h FROM HolidayEntity h JOIN FETCH h.workCalendars c WHERE YEAR(h.date) = :year AND c.id = :calendar")
    Collection<HolidayEntity> getHolidayEntitiesByYear(@Param("year") Integer year, @Param("calendar") Long calendar);

    @Modifying
    @Query(value = "DELETE FROM work_calendar2holiday WHERE work_calendar_id = :calendarId AND holiday_id = :holidayId", nativeQuery = true)
    void deleteHolidayToWorkCalendarMapping(@Param("calendarId") Long calendarId, @Param("holidayId") Long holidayId);
}
