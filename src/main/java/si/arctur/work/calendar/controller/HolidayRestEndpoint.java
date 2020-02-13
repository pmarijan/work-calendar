package si.arctur.work.calendar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.HolidayDTO;
import si.arctur.work.calendar.service.HolidayService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/workcalendar/{calendarId}/holiday")
public class HolidayRestEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(HolidayRestEndpoint.class);

    @Autowired
    private HolidayService holidayService;

    /**
     * Get list of holidays for selected calendarId (mandatory) and optional filter by query parameters for holidays is available
     *
     * @param calendarId - id of the calendar
     * @param name - search holiday by exact name
     * @param date - date of holiday, search format of date is yyyy-DD-mm
     * @param isWorkFree - possible values are true/false
     * @return
     */
    @GetMapping
    @Operation(summary = "Get list of holidays", description = "Get list of holidays for selected calendar filtered by holiday name, date and isWorkFree.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of holidays returned"),
                    @ApiResponse(responseCode = "400", description = "Bad input data", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "No holidays found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public List<HolidayDTO> getHolidays(@PathVariable("calendarId") Long calendarId,
                                        @RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "date", required = false) LocalDate date,
                                        @RequestParam(value = "isWorkFree", required = false) Boolean isWorkFree) {
        LOG.info("START - getHolidays(calendarId={}, name={}, date={}, isWorkFree={})", calendarId, name, date, isWorkFree);

        List<HolidayDTO> result = holidayService.getHolidays(calendarId, date, name, isWorkFree);
        if(result.isEmpty()) {
            LOG.error("No holiday objects found for calendarId={}, date={}, name={}, isWorkFree={} parameters", calendarId, date, name, isWorkFree);
            throw new ResourceNotFoundException("No holiday objects found");
        }
        return result;
    }

    /**
     * Get holiday by selected calendarId and holidayId, both parameters are mandatory
     *
     * @param calendarId
     * @param holidayId
     * @return
     */
    @GetMapping(path = "/{holidayId}")
    @Operation(summary = "Get holiday", description = "Get holiday for selected calendarId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Holiday returned"),
                    @ApiResponse(responseCode = "404", description = "Holiday not found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public HolidayDTO getHoliday(@PathVariable("calendarId") Long calendarId,
                                 @PathVariable("holidayId") Long holidayId) {
        LOG.info("START - getHoliday(calendarId={}, holidayId={})", calendarId, holidayId);

        HolidayDTO holidayDTO = holidayService.getHoliday(calendarId, holidayId);
        if(Objects.isNull(holidayDTO)) {
            LOG.error("No holiday object found for calendarId={} and holidayId={}", calendarId, holidayId);
            throw new ResourceNotFoundException("No holiday object found for calendarId=" + calendarId + "and holidayId=" + holidayId);
        }

        return holidayDTO;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add new holiday", description = "Add new holiday to selected calendar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Holiday successfully added"),
                    @ApiResponse(responseCode = "404", description = "Calendar not found to add new holiday", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public HolidayDTO addHoliday(@PathVariable("calendarId") Long calendarId,
                                 @Valid @NotNull @RequestBody HolidayDTO holidayDTO) {
        LOG.info("START - addHoliday(calendarId={}, holidayDTO={})", calendarId, holidayDTO);

        return holidayService.addHolidayToCalendar(calendarId, holidayDTO);
    }

    @PutMapping(path = "/{holidayId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update existing holiday", description = "Update existing holiday for selected calendar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Holiday successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Calendar or holiday not found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public HolidayDTO updateHoliday(@PathVariable("calendarId") Long calendarId,
                                    @PathVariable("holidayId") Long holidayId,
                                    @NotNull @Valid @RequestBody HolidayDTO holidayDTO) {
        LOG.info("START - updateHoliday(calendarId={}, holidayId={}, holidayDTO={})", calendarId, holidayId, holidayDTO);

        //check if ids match
        if(Objects.nonNull(holidayDTO.getId()) && (holidayId != holidayDTO.getId())) {
            LOG.error("Provided path holidayId={} and holidayDTO.holidayId={} do not match!", holidayId, holidayDTO.getId());
            throw new IllegalArgumentException("Provided path id and holidayDTO.id do not match!");
        }
        holidayDTO.setId(holidayId);

        return holidayService.updateHoliday(calendarId, holidayDTO);
    }

    /**
     * Delete resource and return http status 204 (no content)
     * @param id
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete holiday", description = "Delete holiday for selected calendar.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Holiday successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Calendar or holiday not found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public void deleteHoliday(@PathVariable("calendarId") Long calendarId,
                              @PathVariable("id") Long id) {
        LOG.info("START - deleteHoliday(calendarId={}, id={})", calendarId, id);
        holidayService.deleteHoliday(calendarId, id);
        LOG.info("END - deleteHoliday");
    }
}
