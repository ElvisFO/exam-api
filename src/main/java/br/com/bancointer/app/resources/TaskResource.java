package br.com.bancointer.app.resources;

import br.com.bancointer.app.dto.TaskDTO;
import br.com.bancointer.app.event.ResourceCreateEvent;
import br.com.bancointer.app.mapper.entity.TaskModelMapper;
import br.com.bancointer.app.model.Task;
import br.com.bancointer.app.service.TaskService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Elvis Fernandes on 25/06/19
 */
@RestController
@RequestMapping("/tasks")
@Api(description = "Task related operations")
public class TaskResource {

    private final TaskService service;
    private final ApplicationEventPublisher publisher;
    private final TaskModelMapper modelMapper;

    @Autowired
    public TaskResource(TaskService service, ApplicationEventPublisher publisher, TaskModelMapper modelMapper) {
        this.service = service;
        this.publisher = publisher;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(value = "Return a list of tasks", response = TaskDTO[].class)
    @GetMapping
    public ResponseEntity<List<TaskDTO>> findAll(@ApiParam(value = "Filter Tasks by creation date")
                                                 @RequestParam(value = "createdAt")
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt) {
        return ResponseEntity.ok(this.modelMapper.convertToDtoList(this.service.findByCreatedAt(createdAt)));
    }


    @ApiOperation(value = "Return a job based on it's id", response = TaskDTO.class)
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.modelMapper.convertToDto(this.service.findById(id)));
    }

    @ApiOperation(value = "Create task and return the task created", response = TaskDTO.class)
    @ApiResponses(value = { @ApiResponse( code = 422, message = "Unprocessable Entity")})
    @PostMapping
    public ResponseEntity<TaskDTO> save(@Valid @RequestBody TaskDTO dto, HttpServletResponse response) {
        dto.setId(null);
        Task taskSave = this.service.save(this.modelMapper.convertoToEntity(dto));
        publisher.publishEvent(new ResourceCreateEvent(this, response, taskSave.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.modelMapper.convertToDto(taskSave));
    }

    @ApiOperation(value = "Update task and return 200 Ok with no body")
    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @Valid @RequestBody Task task) {
        Task taskSave = this.service.update(id, task);
        return ResponseEntity.ok(taskSave);
    }

    @ApiOperation(value = "Delete a specific task return 200 Ok with no body")
    @ApiResponses(value = { @ApiResponse( code = 404, message = "Not found")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        this.service.deleteById(id);
    }
}
