package br.com.bancointer.app.resources;

import br.com.bancointer.app.exception.ObjectNotFoundException;
import br.com.bancointer.app.model.Job;
import br.com.bancointer.app.service.JobService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

/**
 * @author Elvis Fernandes on 27/06/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class JobResourcesTest {

    @MockBean
    private JobService service;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Job job = mockJob();

    public static Job mockJob() {
        Job job = new Job("Job 1", true, null, null);
        job.setId(1L);
        return job;
    }

    @Before
    public void setup() {
        BDDMockito.when(service.findById(job.getId())).thenReturn(job);
        BDDMockito.when(service.findAll()).thenReturn(Collections.singletonList(job));
        BDDMockito.when(service.findById(-1L)).thenThrow(new ObjectNotFoundException("error.job.notfound"));

    }

    @Test
    public void listAllShouldReturn200() {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/jobs", GET, null, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getJobByIdWhenJobDoesNotExistsShouldReturn404() {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/jobs/{id}", GET, null, String.class, -1);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void getJobByIdWhenJobExistsShouldReturn200() {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/jobs/1", GET, null, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteJobWhenIdExistsShouldReturn200() {
        long id = 1L;
        BDDMockito.doNothing().when(service).deleteById(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/jobs/{id}", DELETE, null, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteJobWhenIdDoesNotExistsShouldReturn404() {
        long id = -1L;
        BDDMockito.willThrow(new EmptyResultDataAccessException(1)).given(service).deleteById(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/jobs/{id}", DELETE, null, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createJobWhenNameIsNullShouldReturn422() {
        Job job = service.findById(1L);
        job.setName(null);
        assertThat(createJob(job).getStatusCodeValue()).isEqualTo(422);
    }

    private ResponseEntity<String> createJob(Job job) {
        BDDMockito.when(service.save(job)).thenReturn(job);
        return testRestTemplate.exchange("/jobs", POST,
                new HttpEntity<>(job), String.class);
    }
}
