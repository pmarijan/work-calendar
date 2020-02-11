package si.arctur.work.calendar.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import si.arctur.work.calendar.model.DayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WorkCalendarRestEndpointIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetWorkCalendars_getNotEmpty() {
        ResponseEntity<List<WorkCalendarDTO>> calendarList = restTemplate.exchange("/workcalendar", HttpMethod.GET, null, new ParameterizedTypeReference<List<WorkCalendarDTO>>() {});

        Assert.assertNotNull(calendarList);
        Assert.assertFalse(calendarList.getBody().isEmpty());
    }

    @Test
    public void testGetWorkCalendar_getOne() {
        ResponseEntity<WorkCalendarDTO> calendar = restTemplate.exchange("/workcalendar/1", HttpMethod.GET, null, new ParameterizedTypeReference<WorkCalendarDTO>() {});

        WorkCalendarDTO workCalendarDTO = calendar.getBody();

        Assert.assertNotNull(workCalendarDTO);
        Assert.assertFalse(workCalendarDTO.getWorkdays().isEmpty());
    }

    @Test
    public void testGetListOfDays_getNotEmpty() {
        ResponseEntity<List<DayDTO>> days = restTemplate.exchange("/workcalendar/1/day?from=2020-01-01&to=2020-01-31", HttpMethod.GET, null, new ParameterizedTypeReference<List<DayDTO>>() {});

        Assert.assertNotNull(days);
        Assert.assertFalse(days.getBody().isEmpty());
    }

    @Test
    public void testCountWorkDays() {
        ResponseEntity<Long> count = restTemplate.exchange("/workcalendar/1/count?from=2020-01-01&to=2020-01-31", HttpMethod.GET, null, new ParameterizedTypeReference<Long>() {});

        Assert.assertNotNull(count);
        Assert.assertNotNull(count.getBody());
        Assert.assertTrue(count.getBody() > 0);
    }
}
