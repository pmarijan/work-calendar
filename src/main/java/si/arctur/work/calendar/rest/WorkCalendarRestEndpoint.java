package si.arctur.work.calendar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import si.arctur.work.calendar.model.WorkCalendarDTO;
import si.arctur.work.calendar.model.WorkweekDTO;
import si.arctur.work.calendar.service.WorkCalendarService;
import si.arctur.work.calendar.service.WorkweekService;

import java.util.List;

@RestController
@RequestMapping(path = "/workcalendar")
public class WorkCalendarRestEndpoint {

    @Autowired
    private WorkCalendarService workCalendarService;

    @Autowired
    private WorkweekService workweekService;

    @GetMapping
    public List<WorkCalendarDTO> getAllCalendars(@RequestParam(value = "year", required = false) Integer year, @RequestParam(value = "name", required = false) String name) {
        return workCalendarService.getWorkCalendars();
    }

    @GetMapping(path = "/{id}")
    public void getCalendar(@PathVariable("id") Long id) {

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
    public List<WorkweekDTO> getWorkweeksForCalendar(@PathVariable("calendarId") Long calendarId, @RequestParam(value = "weekNumber", required = false) Integer weekNumber, @RequestParam(value = "description", required = false) String description) {
        return workweekService.getWorkweeksByCalendarId(calendarId);
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
