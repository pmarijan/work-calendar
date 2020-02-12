package si.arctur.work.calendar.controller;

import org.apache.tomcat.jni.Local;
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
import si.arctur.work.calendar.dao.entity.HolidayEntity;
import si.arctur.work.calendar.dao.repository.HolidayRepository;
import si.arctur.work.calendar.dao.repository.WorkweekRepository;
import si.arctur.work.calendar.model.HolidayDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HolidayRestEndpointIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WorkweekRepository workweekRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    private HttpHeaders headers = new HttpHeaders();

    @Before
    public void setUp() {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetHolidays_getListOf20() {
        List<HolidayDTO> holidayDTOList = restTemplate.exchange("/workcalendar/1/holiday", HttpMethod.GET, null, new ParameterizedTypeReference<List<HolidayDTO>>() {}).getBody();

        Assert.assertNotNull(holidayDTOList);
        Assert.assertTrue(holidayDTOList.size() == 20);

        HolidayDTO holidayDTO = holidayDTOList.get(0);
        Assert.assertTrue(1 == holidayDTO.getId());
        Assert.assertEquals(LocalDate.of(2020, 1, 1), holidayDTO.getDate());
        Assert.assertEquals("Novo leto", holidayDTO.getName());
        Assert.assertTrue(holidayDTO.getWorkFree());

    }

    @Test
    public void testGetHolidays_getListOf2QueryByName() {
        List<HolidayDTO> holidayDTOList = restTemplate.exchange("/workcalendar/1/holiday?name=Novo leto", HttpMethod.GET, null, new ParameterizedTypeReference<List<HolidayDTO>>() {}).getBody();

        Assert.assertNotNull(holidayDTOList);
        Assert.assertTrue(holidayDTOList.size() == 2);

        HolidayDTO holidayDTO = holidayDTOList.get(1);
        Assert.assertTrue(2 == holidayDTO.getId());
        Assert.assertEquals(LocalDate.of(2020, 1, 2), holidayDTO.getDate());
        Assert.assertEquals("Novo leto", holidayDTO.getName());
        Assert.assertTrue(holidayDTO.getWorkFree());
    }

    @Test
    public void testGetHolidays_get404QueryByDate() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/holiday?date=2091-02-04", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetHolidays_get400QueryByDate() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/holiday?date=2091-22-44", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetHolidays_get404() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/9631/holiday", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetHoliday_getOne() {
        HolidayDTO holidayDTO = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.GET, null, new ParameterizedTypeReference<HolidayDTO>() {}).getBody();

        Assert.assertNotNull(holidayDTO);
        Assert.assertTrue(1 == holidayDTO.getId());
        Assert.assertEquals(LocalDate.of(2020, 1, 1), holidayDTO.getDate());
        Assert.assertEquals("Novo leto", holidayDTO.getName());
        Assert.assertTrue(holidayDTO.getWorkFree());
        Assert.assertTrue(holidayDTO.getWorkCalendars().size() == 1);
    }

    @Test
    public void testGetHoliday_get404QueryByCalendar() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/5442/holiday/1", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetHoliday_get404QueryByHoliday() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/65784", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetHoliday_get400QueryByHoliday() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/blablabla", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAddHoliday_get200(){
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setDate(LocalDate.of(2020, 12, 19));
        holidayDTO.setName("My holiday");
        holidayDTO.setWorkFree(true);

        HttpEntity<HolidayDTO> entity = new HttpEntity<>(holidayDTO, headers);

        ResponseEntity<HolidayDTO> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/", HttpMethod.POST, entity, new ParameterizedTypeReference<HolidayDTO>() {});

        HolidayDTO result = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(holidayDTO.getWorkFree(), result.getWorkFree());
        Assert.assertEquals(holidayDTO.getDate(), result.getDate());
        Assert.assertEquals(holidayDTO.getName(), result.getName());
        Assert.assertNull(holidayDTO.getId());
        Assert.assertNotNull(result.getId());

        Optional<HolidayEntity> savedHolidayEntity = holidayRepository.findById(result.getId());
        Assert.assertEquals(savedHolidayEntity.get().getWorkFree(), result.getWorkFree());
        Assert.assertEquals(savedHolidayEntity.get().getDate(), result.getDate());
        Assert.assertEquals(savedHolidayEntity.get().getName(), result.getName());
        Assert.assertEquals(savedHolidayEntity.get().getId(), result.getId());
    }

    @Test
    public void testAddHoliday_get200ForExistingHoliday() {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setDate(LocalDate.of(2020, 1, 1));
        holidayDTO.setName("Novo leto");
        holidayDTO.setWorkFree(true);

        HttpEntity<HolidayDTO> entity = new HttpEntity<>(holidayDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/", HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testAddHoliday_get404QueryByCalendar(){
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setDate(LocalDate.of(2020, 12, 19));
        holidayDTO.setName("My holiday");
        holidayDTO.setWorkFree(true);

        HttpEntity<HolidayDTO> entity = new HttpEntity<>(holidayDTO, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/999221/holiday/", HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAddHoliday_get400QueryByCalendar(){
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setDate(LocalDate.of(2020, 12, 19));
        holidayDTO.setName("My holiday");
        holidayDTO.setWorkFree(true);

        HttpEntity<HolidayDTO> entity = new HttpEntity<>(holidayDTO, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/565hdfh/holiday/", HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAddHoliday_get400EmptyBody(){
        HttpEntity<HolidayDTO> entity = new HttpEntity<>(new HolidayDTO(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/", HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteHoliday_get204() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        ResponseEntity<String> getResponse = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
        Assert.assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    public void testDeleteHoliday_get404() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1212544/holiday/1", HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateHoliday_get200(){
        //get old value to compare later
        HolidayDTO holidayDTOBeforeUpdate = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.GET, null, new ParameterizedTypeReference<HolidayDTO>() {}).getBody();

        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setDate(LocalDate.of(2020, 12, 19));
        holidayDTO.setName("My holiday");
        holidayDTO.setWorkFree(false);

        HttpEntity<HolidayDTO> entity = new HttpEntity<>(holidayDTO, headers);

        ResponseEntity<HolidayDTO> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.PUT, entity, new ParameterizedTypeReference<HolidayDTO>() {});

        HolidayDTO holidayDTOResponse = responseEntity.getBody();

        Assert.assertNotNull(responseEntity);

        //search for new data to compare with previous
        HolidayDTO updatedHolidayDTO = restTemplate.exchange("/workcalendar/1/holiday/1", HttpMethod.GET, null, new ParameterizedTypeReference<HolidayDTO>() {}).getBody();

        //input should be equal to retrieved object
        Assert.assertEquals(holidayDTO.getName(), updatedHolidayDTO.getName());
        Assert.assertEquals(holidayDTO.getDate(), updatedHolidayDTO.getDate());
        Assert.assertEquals(holidayDTO.getWorkFree(), updatedHolidayDTO.getWorkFree());

        //input should be different from old one
        Assert.assertNotEquals(holidayDTO.getName(), holidayDTOBeforeUpdate.getName());
        Assert.assertNotEquals(holidayDTO.getWorkFree(), holidayDTOBeforeUpdate.getWorkFree());
        Assert.assertNotEquals(holidayDTO.getDate(), holidayDTOBeforeUpdate.getDate());

        //number of calendars must be same after update
        Assert.assertEquals(updatedHolidayDTO.getWorkCalendars().size(), holidayDTOBeforeUpdate.getWorkCalendars().size());
    }

    @Test
    public void testUpdateHoliday_get404QueryByCalendar() {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setDate(LocalDate.of(2020, 12, 19));
        holidayDTO.setName("My holiday");
        holidayDTO.setWorkFree(false);

        HttpEntity<HolidayDTO> entity = new HttpEntity<>(holidayDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/434343/holiday/1", HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateHoliday_get404QueryByHoliday() {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setDate(LocalDate.of(2020, 12, 19));
        holidayDTO.setName("My holiday");
        holidayDTO.setWorkFree(false);

        HttpEntity<HolidayDTO> entity = new HttpEntity<>(holidayDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/175654654", HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateHoliday_get400QueryByHoliday() {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setDate(LocalDate.of(2020, 12, 19));
        holidayDTO.setName("My holiday");
        holidayDTO.setWorkFree(false);

        HttpEntity<HolidayDTO> entity = new HttpEntity<>(holidayDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/workcalendar/1/holiday/null", HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {});

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
