package si.arctur.work.calendar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import si.arctur.work.calendar.model.WorkweekDTO;
import si.arctur.work.calendar.service.WorkweekService;

import java.util.List;

@RestController
@RequestMapping(path = "/workweek")
public class WorkweekRestEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(WorkweekRestEndpoint.class);

    @Autowired
    private WorkweekService workweekService;

    @GetMapping
    public List<WorkweekDTO> getWorkweeks(@RequestParam(value = "description", required = false) String description, @RequestParam(value = "weekNumber", required = false) Integer weekNumber) {
        LOG.info("START - getWorkweeks(description={}, weekNumber={})");
        return workweekService.getWorkweeks(description, weekNumber);
    }

    @GetMapping(path = "/{id}")
    public WorkweekDTO getWorkweek(@PathVariable("id") Long id) {
        LOG.info("START - getWorkweek(id={})", id);
        return workweekService.getWorkweek(id);
    }

    @PostMapping
    public WorkweekDTO addWorkweek(@RequestBody WorkweekDTO workweekDTO) {
        LOG.info("START - addWorkweek(workweekDTO={})", workweekDTO);
        return workweekService.addWorkweek(workweekDTO);
    }

    @PutMapping(path = "/{id}")
    public WorkweekDTO updateWorkweek(@PathVariable("id") Long id, @RequestBody WorkweekDTO workweekDTO) {
        LOG.info("START - updateWorkweek(id={}, workweekDTO={})", id, workweekDTO);

        //check if ids match
        if(id != workweekDTO.getId()) {
            //TODO: throw exception
        }

        return workweekService.updateWorkweek(workweekDTO);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteWorkweek(@PathVariable("id") Long id) {
        LOG.info("START - deleteWorkweek(id={})", id);

        workweekService.deleteWorkweek(id);
    }
}
