package si.arctur.work.calendar.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import si.arctur.work.calendar.model.WorkweekDTO;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WorkweekRestEndpointIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    @Before
    public void setUp() {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    @Ignore
    public void testGetWorkweeks() {
        List<WorkweekDTO> workweekDTOList = restTemplate.exchange("/workcalendar/1/workweek", HttpMethod.GET, null, new ParameterizedTypeReference<List<WorkweekDTO>>() {}).getBody();
    }

    @Test
    @Ignore
    public void testGetWorkweek() {
        WorkweekDTO workweekDTO = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.GET, null, new ParameterizedTypeReference<WorkweekDTO>() {}).getBody();
    }

    @Test
    @Ignore
    public void testAddWorkweek() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);

        WorkweekDTO workweekDTOResponse = restTemplate.exchange("/workcalendar/1/workweek/", HttpMethod.POST, entity, new ParameterizedTypeReference<WorkweekDTO>() {}).getBody();
    }

    @Test
    @Ignore
    public void testUpdateWorkweek() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);

        WorkweekDTO workweekDTOResponse = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.PUT, null, new ParameterizedTypeReference<WorkweekDTO>() {}).getBody();
    }

    @Test
    @Ignore
    public void testDeleteWorkweek() {
        String response = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.DELETE, null, String.class).getBody();
    }
}