package br.com.bancointer.app.mapper.entity;

import br.com.bancointer.app.dto.BaseJobDTO;
import br.com.bancointer.app.dto.JobDTO;
import br.com.bancointer.app.dto.TaskDTO;
import br.com.bancointer.app.exception.ObjectNotFoundException;
import br.com.bancointer.app.mapper.impl.AbstractEntityModelMapper;
import br.com.bancointer.app.model.Job;
import br.com.bancointer.app.model.Task;
import br.com.bancointer.app.repository.JobRepository;
import br.com.bancointer.app.service.JobService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elvis Fernandes on 28/06/19
 */
@Component
public class JobModelMapper extends AbstractEntityModelMapper<Job, JobDTO> {

    private final JobRepository jobRepository;

    @Autowired
    public JobModelMapper(ModelMapper modelMapper, JobRepository jobRepository) {
        super(modelMapper);
        this.jobRepository = jobRepository;
    }

    @Override
    public Converter<Job, JobDTO> toDtoConverter() {

        return mappingContext -> {
            JobDTO destination = mappingContext.getDestination();
            Job parentJob = mappingContext.getSource().getParentJob();
            List<Task> tasks = mappingContext.getSource().getTasks();

            if(tasks != null && tasks.size() > 0) {
                List<TaskDTO> taskList = new ArrayList<>();
                tasks.forEach(taskDto -> {
                    TaskDTO task = TaskDTO.builder()
                            .name(taskDto.getName())
                            .completed(taskDto.getCompleted())
                            .createdAt(taskDto.getCreatedAt())
                            .weight(taskDto.getWeight())
                            .build();
                    task.setId(taskDto.getId());
                    taskList.add(task);
                });

                destination.setTasks(taskList);
            }

            if (null != parentJob) {

                destination.setParentJob(new BaseJobDTO(parentJob.getId(), parentJob.getName(), parentJob.getActive()));
            }
            return destination;
        };
    }

    @Override
    public Converter<JobDTO, Job> toEntityConverter() {

        return mappingContext -> {
            JobDTO dto = mappingContext.getSource();
            Job destination = mappingContext.getDestination();
            destination.setId(null);

            // Retrieve and set parent job according to parentJobId
            if (null != dto.getParentJob() && null != dto.getParentJob().getName()) {
                destination.setParentJob(this.jobRepository.findOneByName(dto.getParentJob().getName()).orElseThrow(() -> new ObjectNotFoundException("error.job.parentrequired")));
            }

            if(dto.getTasks() != null && dto.getTasks().size() > 0) {
                List<Task> taskList = new ArrayList<>();
                dto.getTasks().forEach(taskDto -> {
                    Task task = Task.builder()
                            .name(taskDto.getName())
                            .completed(taskDto.getCompleted())
                            .createdAt(taskDto.getCreatedAt())
                            .weight(taskDto.getWeight())
                            .build();
                    task.setId(taskDto.getId());
                    taskList.add(task);
                });

                destination.setTasks(taskList);
            }

            /*if (null != dto.getId() && dto.getId() > 0L) {
                Job job = jobRepository.findById(dto.getId()).orElseThrow(() -> new ObjectNotFoundException("error.job.notfound"));
                destination.setTasks(job.getTasks());
                destination.setParentJob(job.getParentJob());
            }*/
            return destination;
        };
    }
}
