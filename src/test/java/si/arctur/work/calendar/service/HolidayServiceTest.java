package si.arctur.work.calendar.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.repository.HolidayRepository;
import si.arctur.work.calendar.model.HolidayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HolidayServiceTest {

    @MockBean
    private HolidayRepository holidayRepository;

    @Autowired
    private HolidayService holidayService;

    @Test
    public void testGetHolidays_getOne() {
        Mockito.when(holidayRepository.findAll((Example<HolidayEntity>) Mockito.any())).thenReturn(generateHolidayEntityList(1));

        List<HolidayDTO> holidays = holidayService.getHolidays(LocalDate.of(2020,2,8), null, null);
        Assert.assertNotNull(holidays);
        Assert.assertFalse(holidays.isEmpty());
        Assert.assertTrue(holidays.size() == 1);
    }

    @Test
    public void testGetHolidays_getTwo() {
        Mockito.when(holidayRepository.findAll((Example<HolidayEntity>) Mockito.any())).thenReturn(generateHolidayEntityList(2));

        List<HolidayDTO> holidays = holidayService.getHolidays(LocalDate.of(2020,2,8), null, null);
        Assert.assertNotNull(holidays);
        Assert.assertFalse(holidays.isEmpty());
        Assert.assertTrue(holidays.size() == 2);
    }

    @Test
    public void testGetHolidays_getEmptyCollection() {
        Mockito.when(holidayRepository.findAll((Example<HolidayEntity>) Mockito.any())).thenReturn(new ArrayList<>());

        List<HolidayDTO> holidays = holidayService.getHolidays(LocalDate.of(2020,2,8), "bla bla bla", true);
        Assert.assertNotNull(holidays);
        Assert.assertTrue(holidays.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetHolidaysForCalendar_getExceptionForNullInput() {
        holidayService.getHolidaysForCalendar(null);

        Assert.fail("Exception should be thrown!");
    }

    @Test
    public void testGetHolidaysForCalendar_getCollection() {
        Mockito.when(holidayRepository.getHolidayEntitiesByWorkCalendars(Mockito.any(WorkCalendarEntity.class))).thenReturn(generateHolidayEntityList(5));

        List<HolidayDTO> holidays = holidayService.getHolidaysForCalendar(Long.valueOf(1));

        Assert.assertNotNull(holidays);
        Assert.assertFalse(holidays.isEmpty());
        Assert.assertTrue(holidays.size() == 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetHoliday_getExceptionForNullInput() {
        holidayService.getHoliday(null);

        Assert.fail("Exception should be thrown!");
    }

    @Test
    public void testGetHoliday_getOne() {
        Mockito.when(holidayRepository.getHolidayEntityById(Long.valueOf(1))).thenReturn(generateHolidayEntity(Long.valueOf(1)));

        HolidayDTO result = holidayService.getHoliday(Long.valueOf(1));

        Assert.assertNotNull(result);
        Assert.assertEquals(Long.valueOf(1), result.getId());
    }

    @Test
    public void testGetHoliday_getNull() {
        Mockito.when(holidayRepository.getHolidayEntityById(Long.valueOf(1))).thenReturn(null);

        HolidayDTO result = holidayService.getHoliday(Long.valueOf(1));

        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddHoliday_getExceptionForNullInput() {
        holidayService.addHoliday(null);

        Assert.fail("Exception should be thrown!");
    }

    @Test
    public void testAddHoliday_getOne() {
        Mockito.when(holidayRepository.save(Mockito.any(HolidayEntity.class))).thenReturn(generateHolidayEntity(Long.valueOf(1)));

        HolidayDTO holidayDTO = holidayService.addHoliday(generateHolidayDTO(null));

        Assert.assertNotNull(holidayDTO);
        Assert.assertEquals(Long.valueOf(1), holidayDTO.getId());
        Assert.assertFalse(holidayDTO.getWorkCalendars().isEmpty());
    }

    //methods for generating test data
    private HolidayDTO generateHolidayDTO(Long id) {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(id);
        holidayDTO.setDate(LocalDate.of(2020, 1, 1));
        holidayDTO.setName("test holiday dto object " + id);
        holidayDTO.setWorkFree(true);
        holidayDTO.getWorkCalendars().add(generateWorkCalendarDTO());
        return holidayDTO;
    }

    private WorkCalendarDTO generateWorkCalendarDTO() {
        WorkCalendarDTO workCalendarDTO = new WorkCalendarDTO();
        workCalendarDTO.setId(Long.valueOf(1));
        workCalendarDTO.setYear(2020);
        workCalendarDTO.setName("Test Calendar 1");
        workCalendarDTO.setDescription("Test working calendar 1 for year 2020");
        workCalendarDTO.setWorkdays(EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));

        return workCalendarDTO;
    }

    private List<HolidayEntity> generateHolidayEntityList(int numberOfHolidays) {
        List<HolidayEntity> list = new ArrayList<>();

        for(int i = 1; i <= numberOfHolidays; i++) {
            HolidayEntity holidayEntity = generateHolidayEntity(Long.valueOf(i));

            list.add(holidayEntity);
        }

        return list;
    }

    private HolidayEntity generateHolidayEntity(Long id) {
        HolidayEntity holidayEntity = new HolidayEntity();
        holidayEntity.setId(id);
        holidayEntity.setWorkFree(true);
        holidayEntity.setName("Test holiday " + id);
        holidayEntity.setDate(LocalDate.of(2020, 2, 8));
        holidayEntity.getWorkCalendars().add(generateWorkCalendarEntity());//setWorkCalendars(Stream.of(generateWorkCalendarEntity()).collect(Collectors.toCollection(HashSet::new)));

        return holidayEntity;
    }

    private WorkCalendarEntity generateWorkCalendarEntity() {
        WorkCalendarEntity workCalendarEntity = new WorkCalendarEntity();
        workCalendarEntity.setId(Long.valueOf(1));
        workCalendarEntity.setDescription("Test work calendar");
        workCalendarEntity.setName("Calendar 1");
        workCalendarEntity.setYear(2020);
        workCalendarEntity.setWorkdays(EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

        return workCalendarEntity;
    }
}
