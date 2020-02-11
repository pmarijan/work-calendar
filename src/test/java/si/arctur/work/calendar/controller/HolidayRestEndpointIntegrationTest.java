package si.arctur.work.calendar.controller;

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
import si.arctur.work.calendar.model.HolidayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HolidayRestEndpointIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetHolidays() {
        ResponseEntity<List<HolidayDTO>> responseEntity = restTemplate.exchange("/workcalendar/1/holiday", HttpMethod.GET, null, new ParameterizedTypeReference<List<HolidayDTO>>() {});
    }

    @Test
    public void testGetHoliday() {
        ResponseEntity<HolidayDTO> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.GET, null, new ParameterizedTypeReference<HolidayDTO>() {});
    }

    @Test
    public void testAddHoliday(){
        ResponseEntity<HolidayDTO> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/", HttpMethod.POST, null, new ParameterizedTypeReference<HolidayDTO>() {});
    }

    @Test
    public void testUpdateHoliday(){
        ResponseEntity<HolidayDTO> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.PUT, null, new ParameterizedTypeReference<HolidayDTO>() {});
    }

    @Test
    public void testDeleteHoliday(){
        String result = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.DELETE, null, String.class).getBody();
    }
}
