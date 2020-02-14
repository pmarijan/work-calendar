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
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.repository.CalendarRepository;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.WorkCalendarDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkCalendarServiceTest {
    @MockBean
    private CalendarRepository calendarRepository;

    @Autowired
    private WorkCalendarService workCalendarService;

    @Test
    public void testGetWorkCalendars_getAll() {
        Mockito.when(calendarRepository.findAll((Example<WorkCalendarEntity>) Mockito.any())).thenReturn(TestObjectFactory.generateWorkCalendarEntityList(7));

        List<WorkCalendarDTO> workCalendarDTOList = workCalendarService.getWorkCalendars("desc", "name", EnumSet.noneOf(DayOfWeek.class), 2020);

        Assert.assertNotNull(workCalendarDTOList);
        Assert.assertFalse(workCalendarDTOList.isEmpty());
        Assert.assertEquals(Long.valueOf(1), workCalendarDTOList.get(0).getId());
        Assert.assertEquals(EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), workCalendarDTOList.get(0).getWorkdays());
    }

    @Test
    public void testGetWorkCalendars_getEmptyList() {
        Mockito.when(calendarRepository.findAll((Example<WorkCalendarEntity>) Mockito.any())).thenReturn(Arrays.asList());

        List<WorkCalendarDTO> workCalendarDTOList = workCalendarService.getWorkCalendars("desc", "name", EnumSet.noneOf(DayOfWeek.class), 2020);

        Assert.assertNotNull(workCalendarDTOList);
        Assert.assertTrue(workCalendarDTOList.isEmpty());
    }

    @Test
    public void testGetWorkCalendar_getOne() {
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.eq(Long.valueOf(2)))).thenReturn(TestObjectFactory.generateWorkCalendarEntity(2));

        WorkCalendarDTO workCalendarDTO = workCalendarService.getWorkCalendar(Long.valueOf(2));

        Assert.assertNotNull(workCalendarDTO);
        Assert.assertEquals(Long.valueOf(2), workCalendarDTO.getId());
        Assert.assertEquals(EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), workCalendarDTO.getWorkdays());
        Assert.assertEquals(Integer.valueOf(2020), workCalendarDTO.getYear());
        Assert.assertEquals("Calendar 1", workCalendarDTO.getName());
        Assert.assertEquals("Test work calendar", workCalendarDTO.getDescription());
    }

    @Test
    public void testGetWorkCalendar_getNull() {
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.eq(Long.valueOf(2)))).thenReturn(null);

        WorkCalendarDTO workCalendarDTO = workCalendarService.getWorkCalendar(Long.valueOf(2));

        Assert.assertNull(workCalendarDTO);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getWorkdayCount_getResourceNotFoundExceptionForCalendarId() {
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.eq(Long.valueOf(2)))).thenReturn(null);

        workCalendarService.getWorkdayCount(Long.valueOf(2), LocalDate.of(2020, 01, 01), LocalDate.of(2020, 01, 31));

        Assert.fail("ResourceNotFoundException should be thrown!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWorkdayCount_getIllegalArgumentException() {
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.eq(Long.valueOf(1)))).thenReturn(TestObjectFactory.generateWorkCalendarEntity(1));

        workCalendarService.getWorkdayCount(Long.valueOf(1), LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 31));

        Assert.fail("IllegalArgumentException should be thrown!");
    }

    @Test
    public void getWorkdayCount_get3() {
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.eq(Long.valueOf(1)))).thenReturn(TestObjectFactory.generateWorkCalendarEntity(1));

        Long result = workCalendarService.getWorkdayCount(Long.valueOf(1), LocalDate.of(2020, 02, 01), LocalDate.of(2020, 02, 10));

        Assert.assertNotNull(result);
        Assert.assertEquals(Long.valueOf(3), result);
    }

    @Test
    public void getWorkdayCount_get1() {
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.eq(Long.valueOf(1)))).thenReturn(TestObjectFactory.generateWorkCalendarEntity(1));

        Long result = workCalendarService.getWorkdayCount(Long.valueOf(1), LocalDate.of(2020, 02, 03), LocalDate.of(2020, 02, 03));

        Assert.assertNotNull(result);
        Assert.assertEquals(Long.valueOf(1), result);
    }
}