package si.arctur.work.calendar;

import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.entity.WorkweekEntity;
import si.arctur.work.calendar.model.HolidayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.StringJoiner;

public class TestObjectFactory {

    //methods for generating test data
    public static HolidayDTO generateHolidayDTO(Long id) {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(id);
        holidayDTO.setDate(LocalDate.of(2020, 1, 1));
        holidayDTO.setName("test holiday dto object " + id);
        holidayDTO.setWorkFree(true);
        holidayDTO.getWorkCalendars().add(generateWorkCalendarDTO());
        return holidayDTO;
    }

    public static WorkCalendarDTO generateWorkCalendarDTO() {
        WorkCalendarDTO workCalendarDTO = new WorkCalendarDTO();
        workCalendarDTO.setId((long) 1);
        workCalendarDTO.setYear(2020);
        workCalendarDTO.setName("Test Calendar 1");
        workCalendarDTO.setDescription("Test working calendar 1 for year 2020");
        workCalendarDTO.setWorkdays(EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));

        return workCalendarDTO;
    }

    public static List<WorkweekEntity> generateWorkweekEntityList(int numberOfWorkweeks) {
        List<WorkweekEntity> list = new ArrayList<>();

        for(int i = 1; i <= numberOfWorkweeks; i++) {
            list.add(generateWorkweekEntity(i));
        }
        return list;
    }

    public static WorkweekEntity generateWorkweekEntity(Integer id) {
        WorkweekEntity workweekEntity = new WorkweekEntity();
        workweekEntity.setId(Long.valueOf(id));
        workweekEntity.setDescription("Workweek description " + id);
        workweekEntity.setWeekNumber(id);
        workweekEntity.setWorkCalendar(generateWorkCalendarEntity(1));

        return workweekEntity;
    }
    public static List<HolidayEntity> generateHolidayEntityList(int numberOfHolidays, boolean addWorkCalendar) {
        List<HolidayEntity> list = new ArrayList<>();

        for(int i = 1; i <= numberOfHolidays; i++) {
            HolidayEntity holidayEntity = generateHolidayEntity((long) i, addWorkCalendar);

            list.add(holidayEntity);
        }

        return list;
    }

    public static HolidayEntity generateHolidayEntity(Long id, boolean addWorkCalendar) {
        return generateHolidayEntity(id, addWorkCalendar, LocalDate.of(2020, 2, 8));
    }

    public static HolidayEntity generateHolidayEntity(Long id, boolean addWorkCalendar, LocalDate date) {
        HolidayEntity holidayEntity = new HolidayEntity();
        holidayEntity.setId(id);
        holidayEntity.setWorkFree(true);
        holidayEntity.setName("Test holiday " + id);
        holidayEntity.setDate(date);
        if(addWorkCalendar) {
            holidayEntity.getWorkCalendars().add(generateWorkCalendarEntity(1));
        }
        return holidayEntity;
    }

    public static WorkCalendarEntity generateWorkCalendarEntity(int id) {
        WorkCalendarEntity workCalendarEntity = new WorkCalendarEntity();
        workCalendarEntity.setId((long) id);
        workCalendarEntity.setDescription("Test work calendar");
        workCalendarEntity.setName("Calendar 1");
        workCalendarEntity.setYear(2020);
        workCalendarEntity.setWorkdays(new StringJoiner(",").add(DayOfWeek.MONDAY.name()).add(DayOfWeek.TUESDAY.name()).toString());
        //we set false to prevent infinite loop
        workCalendarEntity.getHolidays().addAll(generateHolidayEntityList(1, false));

        return workCalendarEntity;
    }

    public static List<WorkCalendarEntity> generateWorkCalendarEntityList(int numOfCalendars) {
        List<WorkCalendarEntity> list = new ArrayList<>();

        for(int i = 1; i <= numOfCalendars; i++) {
            list.add(generateWorkCalendarEntity(i));
        }

        return list;
    }
}
