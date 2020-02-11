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
@RequestMapping(path = "/workcalendar/{calendarId}/holiday")
public class HolidayRestEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(HolidayRestEndpoint.class);

    @Autowired
    private HolidayService holidayService;

    @GetMapping
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

    @GetMapping(path = "/{id}")
    public HolidayDTO getHoliday(@PathVariable("calendarId") Long calendarId,
                                 @PathVariable("id") Long id) {
        LOG.info("START - getHoliday(calendarId={}, id={})", calendarId, id);

        HolidayDTO holidayDTO = holidayService.getHoliday(calendarId, id);
        if(Objects.isNull(holidayDTO)) {
            LOG.error("No holiday object found for calendarId={} and id={}", calendarId, id);
            throw new ResourceNotFoundException("No holiday object found for calendarId=" + calendarId + "and id=" + id);
        }

        return holidayDTO;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public HolidayDTO addHoliday(@PathVariable("calendarId") Long calendarId,
                                 @Valid @NotNull @RequestBody HolidayDTO holidayDTO) {
        LOG.info("START - addHoliday(calendarId={}, holidayDTO={})", calendarId, holidayDTO);

        return holidayService.addHolidayToCalendar(calendarId, holidayDTO);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HolidayDTO updateHoliday(@PathVariable("calendarId") Long calendarId,
                                    @PathVariable("id") Long id,
                                    @NotNull @Valid @RequestBody HolidayDTO holidayDTO) {
        LOG.info("START - updateHoliday(calendarId={}, id={}, holidayDTO={})", calendarId, id, holidayDTO);

        //check if ids match
        if(id != holidayDTO.getId()) {
            LOG.error("Provided path id={} and holidayDTO.id={} do not match!", id, holidayDTO.getId());
            throw new IllegalArgumentException("Provided path id and holidayDTO.id do not match!");
        }

        return holidayService.updateHoliday(calendarId, holidayDTO);
    }

    /**
     * Delete resource and return http status 204 (no content)
     * @param id
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHoliday(@PathVariable("calendarId") Long calendarId,
                              @PathVariable("id") Long id) {
        LOG.info("START - deleteHoliday(calendarId={}, id={})", calendarId, id);
        holidayService.deleteHoliday(calendarId, id);
        LOG.info("END - deleteHoliday");
    }
}
