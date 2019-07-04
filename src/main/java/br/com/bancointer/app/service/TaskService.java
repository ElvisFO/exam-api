package br.com.bancointer.app.service;

import br.com.bancointer.app.exception.ObjectNotFoundException;
import br.com.bancointer.app.model.Task;
import br.com.bancointer.app.repository.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Elvis Fernandes on 25/06/19
 */
@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("error.task.notfound"));
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return this.taskRepository.findAll();
    }

    public Task save(Task task) {
        return this.taskRepository.save(task);
    }

    public Task update(Long id, Task task) {

        Task taskData = findById(id);
        BeanUtils.copyProperties(task, taskData, "id");
        return this.taskRepository.save(taskData);
    }

    public void deleteById(Long id) {
        this.taskRepository.deleteById(id);
    }

    public List<Task> findByCreatedAt(LocalDate createdAt) {
        return this.taskRepository.findByCreatedAt(createdAt);
    }
}
