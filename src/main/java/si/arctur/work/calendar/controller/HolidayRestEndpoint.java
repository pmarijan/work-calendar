package si.arctur.work.calendar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import si.arctur.work.calendar.model.HolidayDTO;
import si.arctur.work.calendar.service.HolidayService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/holiday")
public class HolidayRestEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(HolidayRestEndpoint.class);

    @Autowired
    private HolidayService holidayService;

    @GetMapping
    public List<HolidayDTO> getHolidays(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "date", required = false) LocalDate date,
                                        @RequestParam(value = "isWorkFree", required = false) Boolean isWorkFree) {
        LOG.info("START - getHolidays(name={}, date={}, isWorkFree={})", name, date, isWorkFree);
        return holidayService.getHolidays(date, name, isWorkFree);
    }

    @GetMapping(path = "/{id}")
    public HolidayDTO getHoliday(@PathVariable("id") Long id) {
        LOG.info("START - getHoliday(id={})", id);
        return holidayService.getHoliday(id);
    }

    @PostMapping
    public HolidayDTO addHoliday(@Valid @NotNull @RequestBody HolidayDTO holidayDTO) {
        LOG.info("START - addHoliday(holidayDTO={})", holidayDTO);

        return holidayService.addHoliday(holidayDTO);
    }

    @PutMapping(path = "/{id}")
    public HolidayDTO updateHoliday(@PathVariable("id") Long id,
                                    @Valid @NotNull @RequestBody HolidayDTO holidayDTO) {
        LOG.info("START - updateHoliday(id={}, holidayDTO={})", id, holidayDTO);

        //check if ids match
        if(id != holidayDTO.getId()) {
            LOG.error("Provided path id={} and holidayDTO object id={} do not match!", id, holidayDTO.getId());
            throw new IllegalArgumentException("Provided path id and holidayDTO object id do not match!");
        }

        return holidayService.updateHoliday(holidayDTO);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHoliday(@PathVariable("id") Long id) {
        LOG.info("START - deleteHoliday(id={})", id);
        holidayService.deleteHoliday(id);
    }
}
