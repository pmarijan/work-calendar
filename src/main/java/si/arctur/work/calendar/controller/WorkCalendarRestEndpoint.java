package si.arctur.work.calendar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import si.arctur.work.calendar.model.HolidayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;
import si.arctur.work.calendar.model.WorkweekDTO;
import si.arctur.work.calendar.service.HolidayService;
import si.arctur.work.calendar.service.WorkCalendarService;
import si.arctur.work.calendar.service.WorkweekService;
import java.util.List;

@RestController
@RequestMapping(path = "/workcalendar")
public class WorkCalendarRestEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(WorkCalendarRestEndpoint.class);

    @Autowired
    private WorkCalendarService workCalendarService;

    @Autowired
    private WorkweekService workweekService;

    @Autowired
    private HolidayService holidayService;

    @GetMapping
    public List<WorkCalendarDTO> getWorkCalendars(@RequestParam(value = "description", required = false) String description, @RequestParam(value = "year", required = false) Integer year, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "workDays", required = false) String workDays) {
        LOG.info("START - getWorkCalendars(description={}, year={}, name={}, workDays={})", description, year, name, workDays);
        return workCalendarService.getWorkCalendars(description, name, workDays, year);
    }

    @GetMapping(path = "/{id}")
    public WorkCalendarDTO getCalendar(@PathVariable("id") Long id) {
        LOG.info("START - getCalendar(id={})", id);

        return workCalendarService.getWorkCalendar(id);
    }

    //holiday rest endpoints
    @GetMapping(path = "/{calendarId}/holiday")
    public List<HolidayDTO> getHolidaysForCalendar(@PathVariable("calendarId") Long calendarId) {
        LOG.info("START - getHolidaysForCalendar(calendarId={})", calendarId);

        return holidayService.getHolidaysForCalendar(calendarId);
    }

    @GetMapping(path = "/{calendarId}/holiday/{holidayId}")
    public void getHolidayForCalendar(@PathVariable("calendarId") Long calendarId, @PathVariable("holidayId") Long holidayId) {
        LOG.info("START - getHolidayForCalendar(calendarId={}, holidayId)", calendarId, holidayId);
    }

    @DeleteMapping(path = "/{calendarId}/holiday/{holidayId}")
    public void deleteHolidayForCalendar(@PathVariable("calendarId") Long calendarId, @PathVariable("holidayId") Long holidayId) {
        LOG.info("START - deleteHolidayForCalendar(calendarId={}, holidayId)", calendarId, holidayId);
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
