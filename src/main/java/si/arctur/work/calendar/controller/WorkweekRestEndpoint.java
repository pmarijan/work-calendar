package si.arctur.work.calendar.controller;

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

    @GetMapping(path = "/{id}")
    public WorkweekDTO getWorkweek(@PathVariable("calendarId") Long calendarId,
                                   @PathVariable("id") Long id) {
        LOG.info("START - getWorkweek(calendarId={}, id={})", calendarId, id);

        WorkweekDTO result = workweekService.getWorkweek(calendarId, id);
        if(Objects.isNull(result)) {
            LOG.error("No workweek object found for calendarId={} and id={}", calendarId, id);
            throw new ResourceNotFoundException("No workweek object found for calendarId=" +calendarId + " and id=" + id);
        }

        LOG.info("END - getWorkweek: result={}", result);
        return result;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public WorkweekDTO addWorkweek(@PathVariable("calendarId") Long calendarId,
                                   @Valid @NotNull@RequestBody WorkweekDTO workweekDTO) {
        LOG.info("START - addWorkweek(calendarId={}, workweekDTO={})", calendarId, workweekDTO);
        return workweekService.addWorkweek(calendarId, workweekDTO);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WorkweekDTO updateWorkweek(@PathVariable("calendarId") Long calendarId,
                                      @PathVariable("id") Long id,
                                      @Valid @NotNull @RequestBody WorkweekDTO workweekDTO) {
        LOG.info("START - updateWorkweek(calendarId={}, id={}, workweekDTO={})", calendarId, id, workweekDTO);

        //check if ids match, otherwise it might be something wrong with input data
        if(id != workweekDTO.getId()) {
            LOG.error("Provided path id={} and workweekDTO object id={} do not match!", id, workweekDTO.getId());
            throw new IllegalArgumentException("Provided path id and workweekDTO.id do not match!");
        }

        return workweekService.updateWorkweek(calendarId, workweekDTO);
    }

    /**
     * Delete resource and return http status 204 (no content)
     * @param id
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkweek(@PathVariable("calendarId") Long calendarId,
                               @PathVariable("id") Long id) {
        LOG.info("START - deleteWorkweek(calendarId={}, id={})", calendarId, id);

        workweekService.deleteWorkweek(calendarId, id);
    }
}
