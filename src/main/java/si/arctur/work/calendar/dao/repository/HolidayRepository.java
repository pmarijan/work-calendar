package si.arctur.work.calendar.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import java.time.LocalDate;
import java.util.Collection;

public interface HolidayRepository extends JpaRepository<HolidayEntity, Long> {

    @Query(value = "SELECT h.* FROM holiday h LEFT JOIN work_calendar2holiday wc2h ON wc2h.holiday_id = h.id LEFT JOIN work_calendar wc ON wc.id = wc2h.work_calendar_id WHERE h.id = :id AND wc.id = :calendarId", nativeQuery = true)
    HolidayEntity getHolidayEntityByIdAndWorkClendarId(@Param("id") Long id, @Param("calendarId") Long calendarId);

    HolidayEntity getHolidayEntityByDateAndName(LocalDate date, String name);

    @Query(value = "SELECT h.* FROM holiday h " +
            "LEFT JOIN work_calendar2holiday wc2h ON wc2h.holiday_id = h.id " +
            "LEFT JOIN work_calendar wc ON wc.id = wc2h.work_calendar_id " +
            "WHERE wc.id = :calendarId AND ((h.name = :name OR :name IS NULL) AND (h.date = :date OR :date IS NULL) AND (h.work_free = :isWorkFree OR :isWorkFree IS NULL))", nativeQuery = true)
    Collection<HolidayEntity> getHolidayEntites(Long calendarId, LocalDate date, String name, Boolean isWorkFree);

//    @Query(value = "SELECT h.* FROM holiday h " +
//            "LEFT JOIN work_calendar2holiday wc2h ON wc2h.holiday_id = h.id " +
//            "LEFT JOIN work_calendar wc ON wc.id = wc2h.work_calendar_id " +
//            "WHERE wc.id = :calendarId)", nativeQuery = true)
//    Collection<HolidayEntity> getHolidayEntitiesByWorkCalendars(@Param("calendarId") Long calendarId);

//    @Query("SELECT h FROM HolidayEntity h JOIN FETCH h.workCalendars c WHERE YEAR(h.date) = :year AND c.id = :calendar")
//    Collection<HolidayEntity> getHolidayEntitiesByYear(@Param("year") Integer year, @Param("calendar") Long calendar);

    @Modifying
    @Query(value = "DELETE FROM work_calendar2holiday WHERE work_calendar_id = :calendarId AND holiday_id = :holidayId", nativeQuery = true)
    void deleteHolidayToWorkCalendarMapping(@Param("calendarId") Long calendarId, @Param("holidayId") Long holidayId);
}