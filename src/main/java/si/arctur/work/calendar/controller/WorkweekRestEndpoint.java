package si.arctur.work.calendar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.WorkweekDTO;
import si.arctur.work.calendar.service.WorkweekService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/workweek")
public class WorkweekRestEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(WorkweekRestEndpoint.class);

    @Autowired
    private WorkweekService workweekService;

    @GetMapping
    public List<WorkweekDTO> getWorkweeks(@RequestParam(value = "description", required = false) String description,
                                          @RequestParam(value = "weekNumber", required = false) Integer weekNumber) {
        LOG.info("START - getWorkweeks(description={}, weekNumber={})");

        List<WorkweekDTO> result = workweekService.getWorkweeks(description, weekNumber);
        if(result.isEmpty()) {
            LOG.error("No workweek objects found for description={}, weekNumber={} parameters", description, weekNumber);
            throw new ResourceNotFoundException("No workweek objects found");
        }

        LOG.info("END - getWorkweeks: result={}", result);
        return result;
    }

    @GetMapping(path = "/{id}")
    public WorkweekDTO getWorkweek(@PathVariable("id") Long id) {
        LOG.info("START - getWorkweek(id={})", id);

        WorkweekDTO result = workweekService.getWorkweek(id);
        if(Objects.isNull(result)) {
            LOG.error("No workweek object found for id={}", id);
            throw new ResourceNotFoundException("No workweek object found for id=" + id);
        }

        LOG.info("END - getWorkweek: result={}", result);
        return result;
    }

    @PostMapping
    public WorkweekDTO addWorkweek(@Valid @NotNull@RequestBody WorkweekDTO workweekDTO) {
        LOG.info("START - addWorkweek(workweekDTO={})", workweekDTO);
        return workweekService.addWorkweek(workweekDTO);
    }

    @PutMapping(path = "/{id}")
    public WorkweekDTO updateWorkweek(@PathVariable("id") Long id, @Valid @NotNull @RequestBody WorkweekDTO workweekDTO) {
        LOG.info("START - updateWorkweek(id={}, workweekDTO={})", id, workweekDTO);

        //check if ids match, otherwise it might be something wrong with input data
        if(id != workweekDTO.getId()) {
            LOG.error("Provided path id={} and workweekDTO object id={} do not match!", id, workweekDTO.getId());
            throw new IllegalArgumentException("Provided path id and workweekDTO object id do not match!");
        }

        return workweekService.updateWorkweek(workweekDTO);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkweek(@PathVariable("id") Long id) {
        LOG.info("START - deleteWorkweek(id={})", id);

        workweekService.deleteWorkweek(id);
    }
}
