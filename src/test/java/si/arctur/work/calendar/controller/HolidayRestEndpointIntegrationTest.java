package si.arctur.work.calendar.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import si.arctur.work.calendar.model.HolidayDTO;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HolidayRestEndpointIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    @Before
    public void setUp() {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

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
        HolidayDTO holidayDTO = new HolidayDTO();
        HttpEntity<HolidayDTO> entity = new HttpEntity<>(holidayDTO, headers);

        ResponseEntity<HolidayDTO> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/", HttpMethod.POST, entity, new ParameterizedTypeReference<HolidayDTO>() {});
    }

    @Test
    public void testUpdateHoliday(){
        HolidayDTO holidayDTO = new HolidayDTO();
        HttpEntity<HolidayDTO> entity = new HttpEntity<>(holidayDTO, headers);

        ResponseEntity<HolidayDTO> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.PUT, entity, new ParameterizedTypeReference<HolidayDTO>() {});
    }

    @Test
    public void testDeleteHoliday(){
        String result = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.DELETE, null, String.class).getBody();
    }
}
