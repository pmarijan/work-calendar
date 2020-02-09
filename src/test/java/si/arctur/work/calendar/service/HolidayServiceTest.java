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
        Mockito.when(holidayRepository.findAll((Example<HolidayEntity>) Mockito.any())).thenReturn(generateHolidayDTOList(1));

        List<HolidayDTO> holidays = holidayService.getHolidays(LocalDate.of(2020,2,8), null, null);
        Assert.assertNotNull(holidays);
        Assert.assertFalse(holidays.isEmpty());
        Assert.assertTrue(holidays.size() == 1);
    }

    @Test
    public void testGetHolidays_getTwo() {
        Mockito.when(holidayRepository.findAll((Example<HolidayEntity>) Mockito.any())).thenReturn(generateHolidayDTOList(2));

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
    public void testGetHolidaysForCalendar_getException() {
        holidayService.getHolidaysForCalendar(null);

        Assert.fail("Exception should be thrown!");
    }

    //methods for generating test data
    private List<HolidayEntity> generateHolidayDTOList(int numberOfHolidays) {
        List<HolidayEntity> list = new ArrayList<>();

        for(int i = 1; i <= numberOfHolidays; i++) {
            HolidayEntity holidayEntity = new HolidayEntity();
            holidayEntity.setId(i);
            holidayEntity.setWorkFree(true);
            holidayEntity.setName("Test holiday " + i);
            holidayEntity.setDate(LocalDate.of(2020, 2, 8));
            holidayEntity.setWorkCalendars(Stream.of(generateWorkCalendarEntity()).collect(Collectors.toCollection(HashSet::new)));

            list.add(holidayEntity);
        }

        return list;
    }

    private WorkCalendarEntity generateWorkCalendarEntity() {
        WorkCalendarEntity workCalendarEntity = new WorkCalendarEntity();
        workCalendarEntity.setId(1);
        workCalendarEntity.setDescription("Test work calendar");
        workCalendarEntity.setName("Calendar 1");
        workCalendarEntity.setYear(2020);
        workCalendarEntity.setWorkdays(new StringJoiner(",").add(DayOfWeek.MONDAY.name()).add(DayOfWeek.TUESDAY.name()).toString());

        return workCalendarEntity;
    }
}
