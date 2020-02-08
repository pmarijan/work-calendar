package si.arctur.work.calendar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import si.arctur.work.calendar.model.HolidayDTO;
import si.arctur.work.calendar.service.HolidayService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/holiday")
public class HolidayRestEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(HolidayRestEndpoint.class);

    @Autowired
    private HolidayService holidayService;

    @GetMapping
    public List<HolidayDTO> getHolidays(@RequestParam("name") String name, @RequestParam("date") LocalDate date, @RequestParam("isWorkFree") Boolean isWorkFree) {
        LOG.info("START - getHolidays(name={}, date={}, isWorkFree={})", name, date, isWorkFree);
        return holidayService.getHolidays(date, name, isWorkFree);
    }

    @GetMapping(path = "/{id}")
    public HolidayDTO getHoliday(@PathVariable("id") Long id) {
        LOG.info("START - getHoliday(id={})", id);
        return holidayService.getHoliday(id);
    }

    @PostMapping
    public HolidayDTO addHoliday(@RequestBody HolidayDTO holidayDTO) {
        LOG.info("START - addHoliday(holidayDTO={})", holidayDTO);

        return holidayService.addHoliday(holidayDTO);
    }

    @PutMapping(path = "/{id}")
    public HolidayDTO updateHoliday(@PathVariable("id") Long id, @RequestBody HolidayDTO holidayDTO) {
        LOG.info("START - updateHoliday(id={}, holidayDTO={})", id, holidayDTO);

        //check if ids match
        if(id != holidayDTO.getId()) {
            //TODO: throw exception
        }

        return holidayService.updateHoliday(holidayDTO);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteHoliday(@PathVariable("id") Long id) {
        LOG.info("START - deleteHoliday(id={})", id);
        holidayService.deleteHoliday(id);
    }
}
