package si.arctur.work.calendar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import si.arctur.work.calendar.config.StringToEnumSetConverter;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.DayDTO;
import si.arctur.work.calendar.model.HolidayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;
import si.arctur.work.calendar.model.WorkweekDTO;
import si.arctur.work.calendar.service.HolidayService;
import si.arctur.work.calendar.service.WorkCalendarService;
import si.arctur.work.calendar.service.WorkweekService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

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

    //used for mapping string to enumset
    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(DayOfWeek.class, new StringToEnumSetConverter());
    }

    @GetMapping
    public List<WorkCalendarDTO> getWorkCalendars(@RequestParam(value = "description", required = false) String description,
                                                  @RequestParam(value = "year", required = false) Integer year,
                                                  @RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "workDays", required = false) EnumSet<DayOfWeek> workDays) {
        LOG.info("START - getWorkCalendars(description={}, year={}, name={}, workDays={})", description, year, name, workDays);

        List<WorkCalendarDTO> result = workCalendarService.getWorkCalendars(description, name, workDays, year);
        if(result.isEmpty()) {
            LOG.error("No workcalendar objects found for description={}, year={}, name={}, workDays={} parameters", description, year, name, workDays);
            throw new ResourceNotFoundException("No workcalendar objects found");
        }

        LOG.info("END - getWorkCalendars: {}", result);
        return result;
    }

    @GetMapping(path = "/{id}")
    public WorkCalendarDTO getWorkCalendar(@PathVariable("id") Long id) {
        LOG.info("START - getWorkCalendar(id={})", id);

        WorkCalendarDTO result = workCalendarService.getWorkCalendar(id);
        if(Objects.isNull(result)) {
            LOG.error("No workcalendar object found for id={}", id);
            throw new ResourceNotFoundException("No workcalendar object found for id=" + id);
        }

        LOG.info("END - getCalendar: {}", result);
        return result;
    }

    @GetMapping(path = "/{id}/day")
    public List<DayDTO> getListOfDays(@PathVariable(value = "id") Long id,
                                      @RequestParam(value = "from") LocalDate from,
                                      @RequestParam(value = "to") LocalDate to) {
        LOG.info("START - getListOfDays(id={}, from={}, to={})", id, from, to);

        List<DayDTO> days = workCalendarService.getListOfDays(id, from, to);
        if(days.isEmpty()) {
            LOG.error("No resources found for calendar with id={} and date interval from={} to={}", id, from, to);
            throw new ResourceNotFoundException("No resources found ");
        }

        LOG.info("END - getListOfDays={}", days);
        return days;
    }

    /**
     * Count number of workdays - weekends and workfree holidays excluded
     * @param id
     * @param from
     * @param to
     * @return
     */
    @GetMapping(path = "/{id}/count")
    public Long countWorkDays(@PathVariable(value = "id") Long id,
                              @RequestParam(value = "from") LocalDate from,
                              @RequestParam(value = "to") LocalDate to) {
        LOG.info("START - countWorkDays(id={}, from={}, to={})", id, from, to);

        Long workdays = workCalendarService.getWorkdayCount(id, from, to);

        LOG.info("END - countWorkDays={}", workdays);
        return workdays;
    }
}
