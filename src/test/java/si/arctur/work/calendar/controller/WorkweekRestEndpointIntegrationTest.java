package si.arctur.work.calendar.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
    public void testGetWorkweeks_getListOfAllWorkweeks() {
        List<WorkweekDTO> workweekDTOList = restTemplate.exchange("/workcalendar/1/workweek", HttpMethod.GET, null, new ParameterizedTypeReference<List<WorkweekDTO>>() {}).getBody();

        Assert.assertFalse(workweekDTOList.isEmpty());
        Assert.assertTrue(workweekDTOList.size() == 4); //this value may change if new insert statements are added to import.sql script
    }

    @Test
    public void testGetWorkweeks_getListOf1QueryByWeekNumber() {
        List<WorkweekDTO> workweekDTOList = restTemplate.exchange("/workcalendar/1/workweek?weekNumber=1", HttpMethod.GET, null, new ParameterizedTypeReference<List<WorkweekDTO>>() {}).getBody();

        Assert.assertFalse(workweekDTOList.isEmpty());
        Assert.assertTrue(workweekDTOList.size() == 1);

        WorkweekDTO workweekDTO = workweekDTOList.get(0);
        Assert.assertEquals(Integer.valueOf(1), workweekDTO.getWeekNumber());
    }

    @Test
    public void testGetWorkweeks_getListOf1QueryByDescription() {
        List<WorkweekDTO> workweekDTOList = restTemplate.exchange("/workcalendar/1/workweek?description=Work week 2", HttpMethod.GET, null, new ParameterizedTypeReference<List<WorkweekDTO>>() {}).getBody();

        Assert.assertFalse(workweekDTOList.isEmpty());
        Assert.assertTrue(workweekDTOList.size() == 1);

        WorkweekDTO workweekDTO = workweekDTOList.get(0);
        Assert.assertEquals("Work week 2", workweekDTO.getDescription());

    }

    @Test
    public void testGetWorkweeks_get404QueryByDescription() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek?description=some description", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetWorkweeks_get404QueryByWeekNumber() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek?weekNumber=122143", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetWorkweeks_get404QueryByCalendarId() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/87952/workweek?weekNumber=122143", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetWorkweeks_get400QueryByCalendarId() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/badIdNumber/workweek?weekNumber=122143", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetWorkweek_get200() {
        WorkweekDTO workweekDTO = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.GET, null, new ParameterizedTypeReference<WorkweekDTO>() {}).getBody();

        Assert.assertNotNull(workweekDTO);
        Assert.assertEquals(Long.valueOf(1), workweekDTO.getId());
        Assert.assertEquals("Work week 1", workweekDTO.getDescription());
        Assert.assertEquals(Integer.valueOf(1), workweekDTO.getWeekNumber());
        Assert.assertEquals(Long.valueOf(1), workweekDTO.getWorkCalendar().getId());
    }

    @Test
    public void testGetWorkweek_get404QueryByWorkweekId() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek/8787", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetWorkweek_get404QueryByCalendarId() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/6556456/workweek/1", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetWorkweek_get400QueryByCalendarId() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/BadWorkcalendarId/workweek/1", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetWorkweek_get400QueryByWorkweekId() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek/BadWorkweekId", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteWorkweek_get204() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        ResponseEntity<String> responseGetEntity = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseGetEntity.getStatusCode());
    }

    @Test
    public void testDeleteWorkweek_get404QueryByCalendarId() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/98346/workweek/1", HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        //check if resource is still in DB after failed delete action
        //we know workweek with id=1 is under calendarId=1, so we check that combination
        ResponseEntity<String> responseGetEntity = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
        Assert.assertEquals(HttpStatus.OK, responseGetEntity.getStatusCode());
    }

    @Test
    public void testDeleteWorkweek_get404QueryByWorkweekId() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek/98235", HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteWorkweek_get400QueryByCalendarId() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/BadId/workweek/1", HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        //check if resource is still in DB after failed delete action
        //we know workweek with id=1 is under calendarId=1, so we check that combination
        ResponseEntity<String> responseGetEntity = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
        Assert.assertEquals(HttpStatus.OK, responseGetEntity.getStatusCode());
    }

    @Test
    public void testDeleteWorkweek_get400QueryByWorkweekId() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek/BadId", HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAddWorkweek_get200() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        workweekDTO.setDescription("My workweek X");
        workweekDTO.setWeekNumber(50);

        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);

        WorkweekDTO workweekDTOResponse = restTemplate.exchange("/workcalendar/1/workweek/", HttpMethod.POST, entity, new ParameterizedTypeReference<WorkweekDTO>() {}).getBody();

        //compare input and output of post action
        Assert.assertNotNull(workweekDTOResponse);
        Assert.assertEquals(workweekDTO.getDescription(), workweekDTOResponse.getDescription());
        Assert.assertEquals(workweekDTO.getWeekNumber(), workweekDTOResponse.getWeekNumber());

        //we check DB if resource exists
        WorkweekDTO getWorkweeDTO = restTemplate.exchange("/workcalendar/1/workweek/" + workweekDTOResponse.getId(), HttpMethod.GET, null, new ParameterizedTypeReference<WorkweekDTO>() {}).getBody();
        Assert.assertNotNull(workweekDTOResponse);
        Assert.assertEquals(workweekDTO.getDescription(), getWorkweeDTO.getDescription());
        Assert.assertEquals(workweekDTO.getWeekNumber(), getWorkweeDTO.getWeekNumber());
    }

    @Test
    public void testAddWorkweek_get404QueryByCalendar() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        workweekDTO.setDescription("My workweek X");
        workweekDTO.setWeekNumber(50);

        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/66767/workweek", HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAddWorkweek_get400QueryByCalendar() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        workweekDTO.setDescription("My workweek X");
        workweekDTO.setWeekNumber(50);

        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/BadCalendarId/workweek", HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAddWorkweek_get400BadBody() {
        WorkweekDTO workweekDTO = new WorkweekDTO();

        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek", HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateWorkweek_get200() {
        WorkweekDTO workweekDTOBeforeUpdate = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.GET, null, new ParameterizedTypeReference<WorkweekDTO>() {}).getBody();

        WorkweekDTO workweekDTO = new WorkweekDTO();
        workweekDTO.setId(Long.valueOf(1));
        workweekDTO.setDescription("My workweek X");
        workweekDTO.setWeekNumber(50);

        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);

        WorkweekDTO workweekDTOResponse = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.PUT, entity, new ParameterizedTypeReference<WorkweekDTO>() {}).getBody();

        Assert.assertEquals(workweekDTO.getWeekNumber(), workweekDTOResponse.getWeekNumber());
        Assert.assertEquals(workweekDTO.getDescription(), workweekDTOResponse.getDescription());
        Assert.assertEquals(Long.valueOf(1), workweekDTOResponse.getWorkCalendar().getId());

        Assert.assertNotEquals(workweekDTOResponse.getDescription(), workweekDTOBeforeUpdate.getDescription());
        Assert.assertNotEquals(workweekDTOResponse.getWeekNumber(), workweekDTOBeforeUpdate.getWeekNumber());
        Assert.assertEquals(workweekDTOResponse.getId(), workweekDTOBeforeUpdate.getId());
    }

    @Test
    public void testUpdateWorkweek_get500WorkweekIdMismatch() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        workweekDTO.setId(Long.valueOf(564674));
        workweekDTO.setDescription("My workweek X");
        workweekDTO.setWeekNumber(50);

        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek/1", HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateWorkweek_get404QueryByCalendarId() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        workweekDTO.setId(Long.valueOf(1));
        workweekDTO.setDescription("My workweek X");
        workweekDTO.setWeekNumber(50);

        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/99987/workweek/1", HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateWorkweek_get404QueryByWorkweekId() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        workweekDTO.setDescription("My workweek X");
        workweekDTO.setWeekNumber(50);

        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek/53452345", HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateWorkweek_get400BadBody() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek/53452345", HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateWorkweek_get400QueryByCalendarId() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        workweekDTO.setId(Long.valueOf(1));
        workweekDTO.setDescription("My workweek X");
        workweekDTO.setWeekNumber(50);

        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/BadId/workweek/1", HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateWorkweek_get400QueryByWorkweekId() {
        WorkweekDTO workweekDTO = new WorkweekDTO();
        workweekDTO.setId(Long.valueOf(1));
        workweekDTO.setDescription("My workweek X");
        workweekDTO.setWeekNumber(50);

        HttpEntity<WorkweekDTO> entity = new HttpEntity<>(workweekDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/workweek/BadWorkweekId", HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
