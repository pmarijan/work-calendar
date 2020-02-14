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
import si.arctur.work.calendar.TestObjectFactory;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.repository.CalendarRepository;
import si.arctur.work.calendar.dao.repository.HolidayRepository;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.HolidayDTO;
import java.time.LocalDate;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HolidayServiceTest {

    @MockBean
    private HolidayRepository holidayRepository;

    @MockBean
    private CalendarRepository calendarRepository;

    @Autowired
    private HolidayService holidayService;

    @Test
    public void testGetHolidays_getOne() {
        Mockito.when(holidayRepository.getHolidayEntities(Long.valueOf(1), LocalDate.of(2020,2,8), null, null)).thenReturn(TestObjectFactory.generateHolidayEntityList(1));

        List<HolidayDTO> holidays = holidayService.getHolidays(Long.valueOf(1), LocalDate.of(2020,2,8), null, null);
        Assert.assertNotNull(holidays);
        Assert.assertFalse(holidays.isEmpty());
        Assert.assertTrue(holidays.size() == 1);
    }

    @Test
    public void testGetHolidays_getTwo() {
        Mockito.when(holidayRepository.getHolidayEntities(Long.valueOf(1), LocalDate.of(2020,2,8), null, null)).thenReturn(TestObjectFactory.generateHolidayEntityList(2));

        List<HolidayDTO> holidays = holidayService.getHolidays(Long.valueOf(1), LocalDate.of(2020,2,8), null, null);
        Assert.assertNotNull(holidays);
        Assert.assertFalse(holidays.isEmpty());
        Assert.assertTrue(holidays.size() == 2);
    }

    @Test
    public void testGetHolidays_getEmptyCollection() {
        Mockito.when(holidayRepository.findAll((Example<HolidayEntity>) Mockito.any())).thenReturn(new ArrayList<>());

        List<HolidayDTO> holidays = holidayService.getHolidays(Long.valueOf(1), LocalDate.of(2020,2,8), "bla bla bla", true);
        Assert.assertNotNull(holidays);
        Assert.assertTrue(holidays.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetHoliday_getExceptionForNullInput() {
        holidayService.getHoliday(null, null);

        Assert.fail("Exception should be thrown!");
    }

    @Test
    public void testGetHoliday_getOne() {
        Mockito.when(holidayRepository.getHolidayEntityByIdAndWorkCalendarId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(TestObjectFactory.generateHolidayEntity(Long.valueOf(1)));

        HolidayDTO result = holidayService.getHoliday(Long.valueOf(1), Long.valueOf(1));

        Assert.assertNotNull(result);
        Assert.assertEquals(Long.valueOf(1), result.getId());
    }

    @Test
    public void testGetHoliday_getNull() {
        Mockito.when(holidayRepository.getHolidayEntityByIdAndWorkCalendarId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);

        HolidayDTO result = holidayService.getHoliday(Long.valueOf(1), Long.valueOf(1));

        Assert.assertNull(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testAddHoliday_getExceptionForNullCalendarIdInput() {
        holidayService.addHolidayToCalendar(null, TestObjectFactory.generateHolidayDTO(null));

        Assert.fail("Exception should be thrown!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddHoliday_getExceptionForNullHolidayInput() {
        holidayService.addHolidayToCalendar(Long.valueOf(1), null);

        Assert.fail("Exception should be thrown!");
    }

    @Test
    public void testAddHoliday_getOne() {
        Mockito.when(holidayRepository.save(Mockito.any(HolidayEntity.class))).thenReturn(TestObjectFactory.generateHolidayEntity(Long.valueOf(1)));
        Mockito.when(holidayRepository.saveAndFlush(Mockito.any(HolidayEntity.class))).thenReturn(TestObjectFactory.generateHolidayEntity(Long.valueOf(1)));
        Mockito.when(calendarRepository.save(Mockito.any(WorkCalendarEntity.class))).thenReturn(TestObjectFactory.generateWorkCalendarEntity());
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.anyLong())).thenReturn(TestObjectFactory.generateWorkCalendarEntity());

        HolidayDTO holidayDTO = holidayService.addHolidayToCalendar(Long.valueOf(1), TestObjectFactory.generateHolidayDTO(null));

        Assert.assertNotNull(holidayDTO);
        Assert.assertEquals(Long.valueOf(1), holidayDTO.getId());
        Assert.assertFalse(holidayDTO.getWorkCalendars().isEmpty());
    }


}
