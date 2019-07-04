package br.com.bancointer.app.service;

import br.com.bancointer.app.exception.CustomException;
import br.com.bancointer.app.exception.ObjectNotFoundException;
import br.com.bancointer.app.model.Job;
import br.com.bancointer.app.model.Task;
import br.com.bancointer.app.repository.JobRepository;
import br.com.bancointer.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Elvis Fernandes on 25/06/19
 */
@Service
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public JobService(JobRepository jobRepository, TaskRepository taskRepository) {
        this.jobRepository = jobRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public Job findById(Long id) {
        return this.jobRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("error.job.notfound"));
    }

    @Transactional(readOnly = true)
    public List<Job> findAll() {
        return this.jobRepository.findAll();
    }

    public Job save(Job job) {

        this.validateEntity(job);
        Job savedJob = this.jobRepository.save(job);

        if (null != job.getTasks()) {
            List<Task> taskList = new ArrayList<>();
            job.getTasks().forEach(task -> {
                task.setJob(savedJob);
                taskList.add(this.taskRepository.save(task));
            });
            savedJob.setTasks(taskList);
        }
        return savedJob;
    }

    public void deleteById(Long id) {
        this.jobRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    private void validateEntity(Job entity) {

        // Only one job can be root
        if (null == entity.getParentJob()) {
            Optional<Job> rootJob = this.jobRepository.findByParentJobIsNull();
            if (rootJob.isPresent() && !rootJob.get().getId().equals(entity.getId())) {
                throw new CustomException("error.job.parentrequired");
            }
        }

        // Forbid cycle reference
        if (alreadyInTheHierarchy(entity)) {
            throw new CustomException("error.job.cyclereference");
        }

    }

    public Task addTask(final Long jobId, Task task) {
        Job job = findById(jobId);
        task.setJob(job);
        return taskRepository.save(task);
    }

    private boolean alreadyInTheHierarchy(Job job) {

        Job parent = job.getParentJob();

        while(parent != null && !parent.getName().equals(job.getName())) {

            parent = this.jobRepository.findOneByName(parent.getName()).orElse(null);

            if(parent != null && (parent.getParentJob() == null || parent.getParentJob().getName().equals(job.getName()))) {
                parent = parent.getParentJob();
                break;
            } else {
                parent = parent == null ? parent : parent.getParentJob();
            }
        }
        return parent == null ? false : parent.getName().equals(job.getName());
    }
}
