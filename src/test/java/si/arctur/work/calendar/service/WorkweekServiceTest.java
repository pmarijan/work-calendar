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
import si.arctur.work.calendar.dao.entity.WorkweekEntity;
import si.arctur.work.calendar.dao.repository.CalendarRepository;
import si.arctur.work.calendar.dao.repository.HolidayRepository;
import si.arctur.work.calendar.dao.repository.WorkweekRepository;
import si.arctur.work.calendar.exception.ResourceNotFoundException;
import si.arctur.work.calendar.model.WorkweekDTO;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkweekServiceTest {
    @MockBean
    private WorkweekRepository workweekRepository;

    @MockBean
    private CalendarRepository calendarRepository;

    @Autowired
    private WorkweekService workweekService;

    @Test
    public void testGetWorkweeksByCalendarId_getAll() {
        Mockito.when(workweekRepository.getWorkweekEntitiesByWorkCalendar(Mockito.any())).thenReturn(TestObjectFactory.generateWorkweekEntityList(20));

        List<WorkweekDTO> workweekEntityList = workweekService.getWorkweeksByCalendarId(Long.valueOf(1));

        Assert.assertNotNull(workweekEntityList);
        Assert.assertTrue(workweekEntityList.size() == 20);
        Assert.assertEquals(Long.valueOf(1), workweekEntityList.get(0).getId());
        Assert.assertNotNull(workweekEntityList.get(0).getWorkCalendar().getId());
    }

    @Test
    public void testGetWorkweeksByCalendarId_getEmptyList() {
        Mockito.when(workweekRepository.getWorkweekEntitiesByWorkCalendar(Mockito.any())).thenReturn(Arrays.asList());

        List<WorkweekDTO> workweekDTOList = workweekService.getWorkweeksByCalendarId(Long.valueOf(1));

        Assert.assertNotNull(workweekDTOList);
        Assert.assertTrue(workweekDTOList.isEmpty());
    }

    @Test
    public void testGetWorkweek_getOne() {
        Mockito.when(workweekRepository.getWorkweekEntityByIdAndWorkCalendar(Mockito.anyLong(), Mockito.any())).thenReturn(TestObjectFactory.generateWorkweekEntity(99));

        WorkweekDTO workweekDTO = workweekService.getWorkweek(Long.valueOf(1), Long.valueOf(1));

        Assert.assertNotNull(workweekDTO);
        Assert.assertEquals(Long.valueOf(99), workweekDTO.getId());
        Assert.assertEquals(Integer.valueOf(99), workweekDTO.getWeekNumber());
        Assert.assertEquals("Workweek description 99", workweekDTO.getDescription());
        Assert.assertNotNull(workweekDTO.getWorkCalendar());
    }

    @Test
    public void testGetWorkweek_getNull() {
        Mockito.when(workweekRepository.getWorkweekEntityByIdAndWorkCalendar(Mockito.anyLong(), Mockito.any())).thenReturn(null);

        WorkweekDTO workweekDTO = workweekService.getWorkweek(Long.valueOf(1), Long.valueOf(1));

        Assert.assertNull(workweekDTO);
    }

    @Test
    public void testGetWorkweeks_getAll() {
        Mockito.when(workweekRepository.findAll((Example<WorkweekEntity>) Mockito.any())).thenReturn(TestObjectFactory.generateWorkweekEntityList(10));

        List<WorkweekDTO> workweekDTOList = workweekService.getWorkweeks(Long.valueOf(1), "desc 1", 1);

        Assert.assertNotNull(workweekDTOList);
        Assert.assertTrue(workweekDTOList.size() == 10);
        Assert.assertEquals(Long.valueOf(1), workweekDTOList.get(0).getId());
        Assert.assertNotNull(workweekDTOList.get(0).getWorkCalendar().getId());
    }

    @Test
    public void testGetWorkweeks_getEmpty() {
        Mockito.when(workweekRepository.findAll((Example<WorkweekEntity>) Mockito.any())).thenReturn(Arrays.asList());

        List<WorkweekDTO> workweekDTOList = workweekService.getWorkweeks(Long.valueOf(1), "desc 1", 1);

        Assert.assertNotNull(workweekDTOList);
        Assert.assertTrue(workweekDTOList.isEmpty());
    }

    @Test
    public void testAddWorkweek_getOne() {
        Mockito.when(workweekRepository.save(Mockito.any())).thenReturn(TestObjectFactory.generateWorkweekEntity(5));
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.anyLong())).thenReturn(TestObjectFactory.generateWorkCalendarEntity());

        WorkweekDTO result = workweekService.addWorkweek(Long.valueOf(1), new WorkweekDTO());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getWorkCalendar());
        Assert.assertEquals(Long.valueOf(1), result.getWorkCalendar().getId());
        Assert.assertEquals(Long.valueOf(5), result.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testAddWorkweek_getResourceNotFoundException() {
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.anyLong())).thenReturn(null);

        workweekService.addWorkweek(Long.valueOf(1), new WorkweekDTO());

        Assert.fail("ResourceNotFoundException should be thrown");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateWorkweek_getResourceNotFoundException() {
        Mockito.when(workweekRepository.getWorkweekEntityByIdAndWorkCalendar(Mockito.anyLong(), Mockito.any())).thenReturn(null);

        workweekService.updateWorkweek(Long.valueOf(999), new WorkweekDTO());

        Assert.fail("ResourceNotFoundException should be thrown");
    }

    @Test
    public void testUpdateWorkweek_getOne() {
        Mockito.when(workweekRepository.getWorkweekEntityByIdAndWorkCalendar(Mockito.anyLong(), Mockito.any())).thenReturn(TestObjectFactory.generateWorkweekEntity(3));
        Mockito.when(workweekRepository.save(Mockito.any())).thenReturn(TestObjectFactory.generateWorkweekEntity(3));

        WorkweekDTO dto = new WorkweekDTO();
        dto.setId(Long.valueOf(1));
        WorkweekDTO updatedWorkweekDTO = workweekService.updateWorkweek(Long.valueOf(3), dto);

        Assert.assertNotNull(updatedWorkweekDTO);
        Assert.assertEquals(Long.valueOf(3), updatedWorkweekDTO.getId());
        Assert.assertEquals(Integer.valueOf(3), updatedWorkweekDTO.getWeekNumber());
        Assert.assertEquals("Workweek description 3", updatedWorkweekDTO.getDescription());
        Assert.assertTrue(updatedWorkweekDTO.getWorkCalendar().getId() == 1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteWorkweek_getResourceNotFoundForCalendarId() {
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.anyLong())).thenReturn(null);

        workweekService.deleteWorkweek(null, Long.valueOf(1));

        Assert.fail("ResourceNotFoundException should be thrown");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteWorkweek_getResourceNotFoundForWorkweekId() {
        Mockito.when(calendarRepository.getWorkCalendarEntityById(Mockito.anyLong())).thenReturn(TestObjectFactory.generateWorkCalendarEntity());
        Mockito.when(workweekRepository.getWorkweekEntityByIdAndWorkCalendar(Mockito.anyLong(), Mockito.any())).thenReturn(null);

        workweekService.deleteWorkweek(Long.valueOf(1), null);

        Assert.fail("ResourceNotFoundException should be thrown");
    }

    @Test
    public void testDeleteWorkweek_getSuccess() {
        //currently there is no point for mocking and testing this method
    }
}