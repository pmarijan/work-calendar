package si.arctur.work.calendar.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/workcalendar")
public class WorkCalendarRestEndpoint {

    @GetMapping(path = "/")
    public void getAllCalendars() {

    }

    @GetMapping(path = "/{id}")
    public void getCalendarById(@PathVariable("id") Long id) {

    }

    //holiday rest endpoints
    @GetMapping(path = "/{calendarId}/holiday")
    public void getHolidaysForCalendar(@PathVariable("calendarId") Long calendarId) {

    }

    @GetMapping(path = "/{calendarId}/holiday/{holidayId}")
    public void getHolidayForCalendar(@PathVariable("calendarId") Long calendarId, @PathVariable("holidayId") Long holidayId) {

    }

    @DeleteMapping(path = "/{calendarId}/holiday/{holidayId}")
    public void deleteHolidayForCalendar(@PathVariable("calendarId") Long calendarId, @PathVariable("holidayId") Long holidayId) {

    }

    @PostMapping(path = "/{calendarId}/holiday/")
    public void addHolidayForCalendar(@PathVariable("calendarId") Long calendarId, @RequestBody String holiday) {

    }

    @PutMapping(path = "/{calendarId}/holiday/{holidayId}")
    public void updateHolidayForCalendar(@PathVariable("calendarId") Long calendarId, @PathVariable("holidayId") Long holidayId, @RequestBody String holiday) {

    }

    //workweek rest endpoints
    @GetMapping(path = "/{calendarId}/workweek")
    public void getWorkweeksForCalendar(@PathVariable("calendarId") Long calendarId) {

    }

    @GetMapping(path = "/{calendarId}/workweek/{workweekId}")
    public void getWorkweekForCalendar(@PathVariable("calendarId") Long calendarId, @PathVariable("workweekId") Long workweekId) {

    }

    @DeleteMapping(path = "/{calendarId}/workweek/{workweekId}")
    public void deleteWorkweekForCalendar(@PathVariable("calendarId") Long calendarId, @PathVariable("workweekId") Long workweekId) {

    }

    @PostMapping(path = "/{calendarId}/workweek/")
    public void addWorkweekForCalendar(@PathVariable("calendarId") Long calendarId, @RequestBody String workweek) {

    }

    @PutMapping(path = "/{calendarId}/workweek/{workweekId}")
    public void updateWorkweekForCalendar(@PathVariable("calendarId") Long calendarId, @PathVariable("workweekId") Long workweekId, @RequestBody String workweek) {

    }
}
