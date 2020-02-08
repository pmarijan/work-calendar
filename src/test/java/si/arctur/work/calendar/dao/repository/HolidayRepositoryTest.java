package si.arctur.work.calendar.dao.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import java.util.Collection;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HolidayRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    public void testGetHolidayEntityById_getOne() {
        HolidayEntity holidayEntityResult = holidayRepository.getHolidayEntityById(Long.valueOf(1));

        Assert.assertNotNull(holidayEntityResult);
        Assert.assertEquals("Novo leto", holidayEntityResult.getName());
    }

    @Test
    public void testGetHolidayEntityById_getNull() {
        HolidayEntity holidayEntityResult = holidayRepository.getHolidayEntityById(Long.MAX_VALUE);

        Assert.assertNull(holidayEntityResult);
    }

    @Test
    public void testGetHolidayEntitiesByWorkCalendars_getEmptyCollection() {
        Collection<HolidayEntity> holidays = holidayRepository.getHolidayEntitiesByWorkCalendars(new WorkCalendarEntity(Long.MAX_VALUE));

        Assert.assertNotNull(holidays);
        Assert.assertTrue(holidays.isEmpty());
    }

    @Test
    public void testGetHolidayEntitiesByWorkCalendars_getEmptyCollectionForNullInput() {
        Collection<HolidayEntity> holidays = holidayRepository.getHolidayEntitiesByWorkCalendars(null);

        Assert.assertNotNull(holidays);
        Assert.assertTrue(holidays.isEmpty());
    }

    @Test
    public void testGetHolidayEntitiesByWorkCalendars_getNotEmptyCollection() {
        Collection<HolidayEntity> holidays = holidayRepository.getHolidayEntitiesByWorkCalendars(new WorkCalendarEntity(Long.valueOf(1)));

        Assert.assertNotNull(holidays);
        Assert.assertFalse(holidays.isEmpty());
        Assert.assertTrue(holidays.size() == 20);
    }
}
