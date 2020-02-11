package si.arctur.work.calendar.dao.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;

import java.time.DayOfWeek;
import java.util.StringJoiner;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CalendarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CalendarRepository calendarRepository;

//    private WorkCalendarEntity workCalendarEntity;

//    @Before
//    public void setUp() {
//        workCalendarEntity = new WorkCalendarEntity();
//
//        StringJoiner sj = new StringJoiner(",");
//        sj.add(DayOfWeek.MONDAY.name()).add(DayOfWeek.TUESDAY.name()).add(DayOfWeek.WEDNESDAY.name()).add(DayOfWeek.THURSDAY.name()).add(DayOfWeek.FRIDAY.name());
//        workCalendarEntity.setWorkdays(sj.toString());
//        workCalendarEntity.setName("Test Work Calendar 2020");
//        workCalendarEntity.setDescription("Some test description for Test Work Calendar 2020");
//        workCalendarEntity.setYear(2020);
//
//        entityManager.persistAndFlush(workCalendarEntity);
//    }

    @Test
    public void testGetWorkCalendarEntityById_getOne() {
        WorkCalendarEntity workCalendarEntityResult = calendarRepository.getWorkCalendarEntityById(Long.valueOf(1));

        Assert.assertNotNull(workCalendarEntityResult);
        Assert.assertEquals("Some test description for Test Work Calendar 2020", workCalendarEntityResult.getDescription());
        Assert.assertEquals("Test Work Calendar 2020", workCalendarEntityResult.getName());
        Assert.assertEquals(Integer.valueOf(2020), workCalendarEntityResult.getYear());
    }

    @Test
    public void testGetWorkCalendarEntityById_getNull() {
        WorkCalendarEntity workCalendarEntityResult = calendarRepository.getWorkCalendarEntityById(Long.MAX_VALUE);

        Assert.assertNull(workCalendarEntityResult);
    }
}
