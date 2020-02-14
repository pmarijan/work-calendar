package si.arctur.work.calendar.dao.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.entity.WorkweekEntity;

import java.util.Collection;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WorkweekRepositoryTest {

    @Autowired
    private WorkweekRepository workweekRepository;

    @Test
    public void testGetWorkweekEntityById_getOne() {
        WorkweekEntity workweekEntity = workweekRepository.getWorkweekEntityByIdAndWorkCalendar(2L, new WorkCalendarEntity(1L));

        Assert.assertNotNull(workweekEntity);
        Assert.assertEquals(Long.valueOf(2), workweekEntity.getId());
        Assert.assertEquals(Integer.valueOf(2), workweekEntity.getWeekNumber());
        Assert.assertEquals("Work week 2", workweekEntity.getDescription());
        Assert.assertEquals(Long.valueOf(1), workweekEntity.getWorkCalendar().getId());
    }

    @Test
    public void testGetWorkweekEntityById_getNull() {
        WorkweekEntity workweekEntity = workweekRepository.getWorkweekEntityByIdAndWorkCalendar(Long.MAX_VALUE, new WorkCalendarEntity(1L));
        Assert.assertNull(workweekEntity);
    }

    @Test
    public void testGetWorkweekEntityById_getNullForNullInput() {
        WorkweekEntity workweekEntity = workweekRepository.getWorkweekEntityByIdAndWorkCalendar(null, null);
        Assert.assertNull(workweekEntity);
    }

    @Test
    public void testGetWorkweekEntitiesByWorkCalendar_getNotEmptyCollection() {
        Collection<WorkweekEntity> workweekEntities = workweekRepository.getWorkweekEntitiesByWorkCalendar(new WorkCalendarEntity(1L));

        Assert.assertNotNull(workweekEntities);
        Assert.assertFalse(workweekEntities.isEmpty());
        Assert.assertTrue(workweekEntities.size() == 4);
    }

    @Test
    public void testGetWorkweekEntitiesByWorkCalendar_getEmptyCollection() {
        Collection<WorkweekEntity> workweekEntities = workweekRepository.getWorkweekEntitiesByWorkCalendar(new WorkCalendarEntity(Long.MAX_VALUE));

        Assert.assertNotNull(workweekEntities);
        Assert.assertTrue(workweekEntities.isEmpty());
    }

    @Test
    public void testGetWorkweekEntitiesByWorkCalendar_getEmptyCollectionForNullInput() {
        Collection<WorkweekEntity> workweekEntities = workweekRepository.getWorkweekEntitiesByWorkCalendar(null);

        Assert.assertNotNull(workweekEntities);
        Assert.assertTrue(workweekEntities.isEmpty());
    }
}
