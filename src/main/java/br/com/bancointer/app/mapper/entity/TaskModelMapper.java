package br.com.bancointer.app.mapper.entity;

import br.com.bancointer.app.dto.BaseJobDTO;
import br.com.bancointer.app.dto.JobDTO;
import br.com.bancointer.app.dto.TaskDTO;
import br.com.bancointer.app.exception.ObjectNotFoundException;
import br.com.bancointer.app.mapper.impl.AbstractEntityModelMapper;
import br.com.bancointer.app.model.Job;
import br.com.bancointer.app.model.Task;
import br.com.bancointer.app.repository.JobRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elvis Fernandes on 01/07/19
 */
@Component
public class TaskModelMapper extends AbstractEntityModelMapper<Task, TaskDTO> {

    @Autowired
    public TaskModelMapper(ModelMapper modelMapper) {
        super(modelMapper);;
    }

    @Override
    public Converter<Task, TaskDTO> toDtoConverter() {

        return mappingContext ->  mappingContext.getDestination();
    }

    @Override
    public Converter<TaskDTO, Task> toEntityConverter() {

        return mappingContext ->  mappingContext.getDestination();
    }
}
