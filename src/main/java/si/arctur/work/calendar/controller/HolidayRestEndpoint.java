package si.arctur.work.calendar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

        List<HolidayDTO> result = holidayService.getHolidays(date, name, isWorkFree);
        if(result.isEmpty()) {
            LOG.error("No holiday objects found for date={}, name={}, isWorkFree={} parameters", date, name, isWorkFree);
            throw new ResourceNotFoundException("No holiday objects found");
        }
        return result;
    }

    @GetMapping(path = "/{id}")
    public HolidayDTO getHoliday(@PathVariable("id") Long id) {
        LOG.info("START - getHoliday(id={})", id);

        HolidayDTO holidayDTO = holidayService.getHoliday(id);
        if(Objects.isNull(holidayDTO)) {
            LOG.error("No holiday object found for id={}", id);
            throw new ResourceNotFoundException("No holiday object found for id=" + id);
        }

        return holidayDTO;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public HolidayDTO addHoliday(@Valid @NotNull @RequestBody HolidayDTO holidayDTO) {
        LOG.info("START - addHoliday(holidayDTO={})", holidayDTO);

        return holidayService.addHoliday(holidayDTO);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HolidayDTO updateHoliday(@PathVariable("id") Long id,
                                    @NotNull @Valid @RequestBody HolidayDTO holidayDTO) {
        LOG.info("START - updateHoliday(id={}, holidayDTO={})", id, holidayDTO);

        //check if ids match
        if(id != holidayDTO.getId()) {
            LOG.error("Provided path id={} and holidayDTO.id={} do not match!", id, holidayDTO.getId());
            throw new IllegalArgumentException("Provided path id and holidayDTO.id do not match!");
        }

        return holidayService.updateHoliday(holidayDTO);
    }

    /**
     * Delete resource and return http status 204 (no content)
     * @param id
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHoliday(@PathVariable("id") Long id) {
        LOG.info("START - deleteHoliday(id={})", id);
        holidayService.deleteHoliday(id);
        LOG.info("END - deleteHoliday");
    }
}
