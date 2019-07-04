package br.com.bancointer.app.resources;

import br.com.bancointer.app.dto.JobDTO;
import br.com.bancointer.app.event.ResourceCreateEvent;
import br.com.bancointer.app.mapper.entity.JobModelMapper;
import br.com.bancointer.app.model.Job;
import br.com.bancointer.app.model.Task;
import br.com.bancointer.app.service.JobService;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Elvis Fernandes on 25/06/19
 */
@RestController
@RequestMapping("/jobs")
@Api(description = "Job related operations")
public class JobResource {

    private final JobService service;
    private final ApplicationEventPublisher publisher;
    private final JobModelMapper modelMapper;

    @Autowired
    public JobResource(JobService service, ApplicationEventPublisher publisher, JobModelMapper modelMapper) {
        this.service = service;
        this.publisher = publisher;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(value = "Return a list of jobs", response = JobDTO[].class)
    @GetMapping
    public ResponseEntity<List<JobDTO>> findAll(@ApiParam(value = "Sort by the total weight of Tasks inside a Job. Highest values first")
                                                @RequestParam(value = "sortByWeight", defaultValue = "false") boolean sortByWeight) {

        if (!sortByWeight) {
            return ResponseEntity.ok(this.modelMapper.convertToDtoList(this.service.findAll()));
        }

        return ResponseEntity.ok(this.modelMapper.convertToDtoList(this.service.findAll().stream()
                .sorted(Comparator.comparingInt(Job::getTotalWeight).reversed()).collect(Collectors.toList())));
    }

    @ApiOperation(value = "Return a job based on it's id", response = JobDTO.class)
    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.modelMapper.convertToDto(this.service.findById(id)));
    }

    @ApiOperation(value = "Create job and return the job created", response = JobDTO.class)
    @ApiResponses(value = { @ApiResponse( code = 422, message = "Unprocessable Entity")})
    @PostMapping
    public ResponseEntity<JobDTO> save(@Valid @RequestBody JobDTO dto, HttpServletResponse response) {
        Job jobSave = this.service.save(this.modelMapper.convertoToEntity(dto));
        this.publisher.publishEvent(new ResourceCreateEvent(this, response, jobSave.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.modelMapper.convertToDto(jobSave));
    }

    @ApiOperation(value = "Update job and return 200 Ok with no body")
    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> update(@PathVariable Long id, @Valid @RequestBody JobDTO dto) {
        Job job = this.service.findById(id);
        BeanUtils.copyProperties(dto, job, "id");
        Job jobSave = this.service.save(job);
        return ResponseEntity.ok(this.modelMapper.convertToDto(jobSave));
    }

    @ApiOperation(value = "Delete a specific job return 200 Ok with no body")
    @ApiResponses(value = { @ApiResponse( code = 404, message = "Not found")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        this.service.deleteById(id);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getTasks(@PathVariable("id") Long id) {
        Job job = this.service.findById(id);
        return ResponseEntity.ok(job.getTasks());
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable("id") Long id, @Valid @RequestBody Task taskDTO) {
        Task task = this.service.addTask(id, taskDTO);
        return ResponseEntity.ok(task);
    }
}
