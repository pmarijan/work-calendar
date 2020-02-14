package si.arctur.work.calendar.dao.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CalendarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CalendarRepository calendarRepository;

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
