package br.com.bancointer.app.resources;

import br.com.bancointer.app.dto.TaskDTO;
import br.com.bancointer.app.exception.ObjectNotFoundException;
import br.com.bancointer.app.model.Job;
import br.com.bancointer.app.model.Task;
import br.com.bancointer.app.service.JobService;
import br.com.bancointer.app.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

/**
 * @author Elvis Fernandes on 27/06/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskResourcesTest {

    @MockBean
    private TaskService service;

    @Autowired
    private TestRestTemplate testRestTemplate;

    LocalDate now = LocalDate.now();

    private Task task = mockTask();

    public static Task mockTask() {
        Task task = new Task("First Task", 5, true, LocalDate.now(), null);
        task.setId(1L);
        return task;
    }

    @Before
    public void setup() {
        BDDMockito.when(service.findById(task.getId())).thenReturn(task);
        BDDMockito.when(service.findByCreatedAt(now)).thenReturn(Collections.singletonList(task));
        BDDMockito.when(service.findById(-1L)).thenThrow(new ObjectNotFoundException("error.task.notfound"));

    }

    @Test
    public void listAllTasksWhenTheCreationDateExistsShouldReturnNotEmptyList() {
        String URL = String.format("/tasks?createdAt=%s", now);
        ResponseEntity<List<TaskDTO>> exchange = testRestTemplate.exchange(URL, GET, null, new ParameterizedTypeReference<List<TaskDTO>>() {
        });
        assertThat(exchange.getBody()).isNotEmpty();
    }

    @Test
    public void listAllTasksWhenTheCreationDateExistsShouldReturnEmptyList() {
        String URL = String.format("/tasks?createdAt=%s", LocalDate.of(2019, 8, 26));
        ResponseEntity<List<TaskDTO>> exchange = testRestTemplate.exchange(URL, GET, null, new ParameterizedTypeReference<List<TaskDTO>>() {
        });
        assertThat(exchange.getBody()).isEmpty();
    }

    @Test
    public void getTaskByIdWhenTaskDoesNotExistsShouldReturn404() {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/tasks/{id}", GET, null, String.class, -1);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void getTaskByIdWhenTaskExistsShouldReturn200() {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/tasks/1", GET, null, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteTaskWhenIdExistsShouldReturn200() {
        long id = 1L;
        BDDMockito.doNothing().when(service).deleteById(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/tasks/{id}", DELETE, null, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteTaskWhenIdDoesNotExistsShouldReturn404() {
        long id = -1L;
        BDDMockito.willThrow(new EmptyResultDataAccessException(1)).given(service).deleteById(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/tasks/{id}", DELETE, null, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createJobWhenNameIsNullShouldReturn422() {
        Task task = service.findById(1L);
        task.setName(null);
        assertThat(createTask(task).getStatusCodeValue()).isEqualTo(422);
    }

    private ResponseEntity<String> createTask(Task task) {
        BDDMockito.when(service.save(task)).thenReturn(task);
        return testRestTemplate.exchange("/tasks", POST,
                new HttpEntity<>(task), String.class);
    }
}
