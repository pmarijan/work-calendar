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
import si.arctur.work.calendar.model.WorkweekDTO;
import si.arctur.work.calendar.service.WorkweekService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/workcalendar/{calendarId}/workweek")
public class WorkweekRestEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(WorkweekRestEndpoint.class);

    @Autowired
    private WorkweekService workweekService;

    @GetMapping
    @Operation(summary = "Get list of workweeks", description = "Get list of workweeks for selected calendar filtered by workweek description and weekNumber.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of workweeks returned"),
                    @ApiResponse(responseCode = "404", description = "No workweeks found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public List<WorkweekDTO> getWorkweeks(@PathVariable("calendarId") Long calendarId,
                                          @RequestParam(value = "description", required = false) String description,
                                          @RequestParam(value = "weekNumber", required = false) Integer weekNumber) {
        LOG.info("START - getWorkweeks(calendarId={}, description={}, weekNumber={})", calendarId, description, weekNumber);

        List<WorkweekDTO> result = workweekService.getWorkweeks(calendarId, description, weekNumber);
        if(result.isEmpty()) {
            LOG.error("No workweek objects found for calendarId={}, description={}, weekNumber={} parameters", calendarId, description, weekNumber);
            throw new ResourceNotFoundException("No workweek objects found");
        }

        LOG.info("END - getWorkweeks: result={}", result);
        return result;
    }

    @GetMapping(path = "/{workweekId}")
    @Operation(summary = "Get workweek", description = "Get workweek for selected calendarId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workweek returned"),
                    @ApiResponse(responseCode = "404", description = "Workweek not found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public WorkweekDTO getWorkweek(@PathVariable("calendarId") Long calendarId,
                                   @PathVariable("workweekId") Long workweekId) {
        LOG.info("START - getWorkweek(calendarId={}, workweekId={})", calendarId, workweekId);

        WorkweekDTO result = workweekService.getWorkweek(calendarId, workweekId);
        if(Objects.isNull(result)) {
            LOG.error("No workweek object found for calendarId={} and workweekId={}", calendarId, workweekId);
            throw new ResourceNotFoundException("No workweek object found for calendarId=" + calendarId + " and workweekId=" + workweekId);
        }

        LOG.info("END - getWorkweek: result={}", result);
        return result;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add new workweek", description = "Add new workweek to selected calendar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workweek successfully added"),
                    @ApiResponse(responseCode = "404", description = "Calendar not found to add new workweek", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public WorkweekDTO addWorkweek(@PathVariable("calendarId") Long calendarId,
                                   @Valid @NotNull@RequestBody WorkweekDTO workweekDTO) {
        LOG.info("START - addWorkweek(calendarId={}, workweekDTO={})", calendarId, workweekDTO);
        return workweekService.addWorkweek(calendarId, workweekDTO);
    }

    @PutMapping(path = "/{workweekId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update existing workweek", description = "Update existing workweek for selected calendar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workweek successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Calendar or workweek not found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public WorkweekDTO updateWorkweek(@PathVariable("calendarId") Long calendarId,
                                      @PathVariable("workweekId") Long workweekId,
                                      @Valid @NotNull @RequestBody WorkweekDTO workweekDTO) {
        LOG.info("START - updateWorkweek(calendarId={}, workweekId={}, workweekDTO={})", calendarId, workweekId, workweekDTO);

        //check if ids match, otherwise it might be something wrong with input data
        if(workweekId != workweekDTO.getId()) {
            LOG.error("Provided path calendarId={} and workweekDTO object workweekId={} do not match!", workweekId, workweekDTO.getId());
            throw new IllegalArgumentException("Provided path calendarId and workweekDTO.workweekId do not match!");
        }

        return workweekService.updateWorkweek(calendarId, workweekDTO);
    }

    /**
     * Delete resource and return http status 204 (no content)
     * @param workweekId
     */
    @DeleteMapping(path = "/{workweekId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete workweek", description = "Delete workweek for selected calendar.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Workweek successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Calendar or workweek not found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Something went wrong or invalid data was provided", content = @Content())})
    public void deleteWorkweek(@PathVariable("calendarId") Long calendarId,
                               @PathVariable("workweekId") Long workweekId) {
        LOG.info("START - deleteWorkweek(calendarId={}, workweekId={})", calendarId, workweekId);

        workweekService.deleteWorkweek(calendarId, workweekId);
    }
}
