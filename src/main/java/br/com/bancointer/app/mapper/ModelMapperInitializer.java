package br.com.bancointer.app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Elvis Fernandes on 30/06/19
 */
@Component
public class ModelMapperInitializer {

    private final ModelMapper modelMapper;
    private final List<EntityModelMapper> entityModelMapperList;

    @Autowired
    public ModelMapperInitializer(ModelMapper modelMapper, ApplicationContext applicationContext, List<EntityModelMapper> entityModelMapperList) {
        this.modelMapper = modelMapper;
        this.entityModelMapperList = entityModelMapperList;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        entityModelMapperList.forEach(entityModelMapper -> entityModelMapper.configure(modelMapper));
    }

}