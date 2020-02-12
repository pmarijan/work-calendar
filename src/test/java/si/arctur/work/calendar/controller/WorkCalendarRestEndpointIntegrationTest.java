package si.arctur.work.calendar.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import si.arctur.work.calendar.model.DayDTO;
import si.arctur.work.calendar.model.WorkCalendarDTO;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WorkCalendarRestEndpointIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetWorkCalendars_getListWith2Elements() {
        ResponseEntity<List<WorkCalendarDTO>> responseEntity = restTemplate.exchange("/workcalendar", HttpMethod.GET, null, new ParameterizedTypeReference<List<WorkCalendarDTO>>() {});

        List<WorkCalendarDTO> calendarDTOList = responseEntity.getBody();

        Assert.assertNotNull(calendarDTOList);
        Assert.assertFalse(calendarDTOList.isEmpty());
        Assert.assertTrue(calendarDTOList.size() == 2);
    }

    @Test
    public void testGetWorkCalendars_getListWith1Element() {
        ResponseEntity<List<WorkCalendarDTO>> responseEntity = restTemplate.exchange("/workcalendar?year=2020", HttpMethod.GET, null, new ParameterizedTypeReference<List<WorkCalendarDTO>>() {});

        List<WorkCalendarDTO> calendarDTOList = responseEntity.getBody();

        Assert.assertNotNull(calendarDTOList);
        Assert.assertFalse(calendarDTOList.isEmpty());
        Assert.assertTrue(calendarDTOList.size() == 1);
    }

    @Test
    public void testGetWorkCalendars_get404QueryByYear() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar?year=2999", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity.getStatusCode());
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetWorkCalendars_get404QueryByName() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar?name=This name not exist123", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity.getStatusCode());
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetWorkCalendars_getListForQueryByName() {
        ResponseEntity<List<WorkCalendarDTO>> responseEntity = restTemplate.exchange("/workcalendar?name=Test Work Calendar 2020", HttpMethod.GET, null, new ParameterizedTypeReference<List<WorkCalendarDTO>>() {});

        List<WorkCalendarDTO> calendarDTOList = responseEntity.getBody();

        Assert.assertNotNull(calendarDTOList);
        Assert.assertFalse(calendarDTOList.isEmpty());
    }

    @Test
    public void testGetWorkCalendar_getOne() {
        ResponseEntity<WorkCalendarDTO> responseEntity = restTemplate.exchange("/workcalendar/1", HttpMethod.GET, null, new ParameterizedTypeReference<WorkCalendarDTO>() {});

        WorkCalendarDTO workCalendarDTO = responseEntity.getBody();

        Assert.assertNotNull(workCalendarDTO);
        Assert.assertFalse(workCalendarDTO.getWorkdays().isEmpty());
    }

    @Test
    public void testGetWorkCalendar_get404QueryById() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/987652", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetWorkCalendar_get400QueryById() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/blabla", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testCountWorkDays_get21WorkDays() {
        Long count = restTemplate.exchange("/workcalendar/1/count?from=2020-01-01&to=2020-01-31", HttpMethod.GET, null, new ParameterizedTypeReference<Long>() {}).getBody();

        Assert.assertNotNull(count);
        Assert.assertTrue(count == 21);  //this value may change if import.sql script is modified
    }

    @Test
    public void testCountWorkDays_get500() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/2/count?from=2020-01-01&to=2020-01-31", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());  //this value may change if import.sql script is modified
    }

    @Test
    public void testCountWorkDays_get1WorkDays() {
        Long count = restTemplate.exchange("/workcalendar/1/count?from=2020-01-01&to=2020-01-03", HttpMethod.GET, null, new ParameterizedTypeReference<Long>() {}).getBody();

        Assert.assertNotNull(count);
        Assert.assertTrue(count == 1);  //this value may change if import.sql script is modified
    }

    @Test
    public void testCountWorkDays() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/999/count?from=2020-01-01&to=2020-01-31", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetListOfDays_getNotEmpty() {
        ResponseEntity<List<DayDTO>> responseEntity = restTemplate.exchange("/workcalendar/1/day?from=2020-01-01&to=2020-01-31", HttpMethod.GET, null, new ParameterizedTypeReference<List<DayDTO>>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertFalse(responseEntity.getBody().isEmpty());
        Assert.assertTrue(responseEntity.getBody().size() == 31);

        DayDTO day = responseEntity.getBody().get(0);
        Assert.assertEquals(LocalDate.of(2020, 1, 1), day.getDate());
        Assert.assertEquals("Novo leto", day.getHoliday().getName());
        Assert.assertTrue(day.getHoliday().getWorkFree());
    }

    @Test
    public void testGetListOfDays_get404() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/999/day?from=2020-01-01&to=2020-01-31", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
