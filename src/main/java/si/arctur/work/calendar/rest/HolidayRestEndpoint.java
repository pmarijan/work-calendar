package si.arctur.work.calendar.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/holiday")
public class HolidayRestEndpoint {

    @GetMapping(path = "/")
    public void getHolidays() {
        //return all holidays
    }

    @GetMapping(path = "/{id}")
    public void getHoliday(@PathVariable("id") Long id) {
        //return holiday by ID
    }

    @PostMapping(path = "/")
    public void addHoliday(@RequestBody String holiday) {
        //create new holiday
    }

    @PutMapping(path = "/{id}")
    public void updateHoliday(@PathVariable("id") Long id, @RequestBody String holiday) {
        //update holiday
    }

    @DeleteMapping(path = "/{id}")
    public void deleteHoliday(@PathVariable("id") Long id) {
        //delete holiday
    }
}
