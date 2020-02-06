package si.arctur.work.calendar.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/workweek")
public class WorkweekRestEndpoint {

    @GetMapping(path = "/")
    public void getWorkweeks() {
        //return all workweeks
    }

    @GetMapping(path = "/{id}")
    public void getWorkweek(@PathVariable("id") Long id) {
        //return workweek
    }

    @PostMapping(path = "/")
    public void addWorkweek(@RequestBody String workweek) {

    }

    @PutMapping(path = "/{id}")
    public void updateWorkweek(@PathVariable("id") Long id, @RequestBody String workweek) {

    }

    @DeleteMapping(path = "/{id}")
    public void deleteWorkweek(@PathVariable("id") Long id) {

    }
}
