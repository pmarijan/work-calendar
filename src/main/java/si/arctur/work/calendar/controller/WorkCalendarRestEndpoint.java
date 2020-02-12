package si.arctur.work.calendar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import si.arctur.work.calendar.config.StringToEnumSetConverter;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.DayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;
import si.arctur.work.calendar.service.WorkCalendarService;
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

    //used for mapping string to enumset
    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(DayOfWeek.class, new StringToEnumSetConverter());
    }

    /**
     * Get list of calendars, if no query parameters are provided all calendars are returned
     *
     * @param description
     * @param year
     * @param name
     * @param workDays
     * @return
     */
    @GetMapping
    @Operation(summary = "Get list of work calendars", description = "Get list of work calendars filtered by descritpion, year, name and workdays.",
            responses = {
                @ApiResponse(responseCode = "200", description = "List of work calendars returned"),
                @ApiResponse(responseCode = "404", description = "No work calendars found", content = @Content()),
                @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
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
    @Operation(summary = "Get work calendar", description = "Get work calendar for provided id.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Work calendar returned"),
                @ApiResponse(responseCode = "404", description = "Work calendar not found", content = @Content()),
                @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
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
    @Operation(summary = "Get list of days", description = "Get list of days for selected interval.",
            responses = {
                @ApiResponse(responseCode = "200", description = "List of days returned"),
                @ApiResponse(responseCode = "404", description = "No days found", content = @Content()),
                @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
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
    @Operation(summary = "Count number of working days", description = "Count all working days for selected interval.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Return number of working days"),
                @ApiResponse(responseCode = "404", description = "No resources found for calendarId", content = @Content()),
                @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public Long countWorkDays(@PathVariable(value = "id") Long id,
                              @RequestParam(value = "from") LocalDate from,
                              @RequestParam(value = "to") LocalDate to) {
        LOG.info("START - countWorkDays(id={}, from={}, to={})", id, from, to);

        Long workdays = workCalendarService.getWorkdayCount(id, from, to);

        LOG.info("END - countWorkDays={}", workdays);
        return workdays;
    }
}
