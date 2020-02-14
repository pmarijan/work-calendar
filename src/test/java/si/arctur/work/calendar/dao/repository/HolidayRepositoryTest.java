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
        HolidayEntity holidayEntityResult = holidayRepository.getHolidayEntityByIdAndWorkCalendarId(1L, 1L);

        Assert.assertNotNull(holidayEntityResult);
        Assert.assertEquals("Novo leto", holidayEntityResult.getName());
    }

    @Test
    public void testGetHolidayEntityById_getNull() {
        HolidayEntity holidayEntityResult = holidayRepository.getHolidayEntityByIdAndWorkCalendarId(Long.MAX_VALUE, 1L);

        Assert.assertNull(holidayEntityResult);
    }
}
