package br.com.bancointer.app.mapper.impl;

import br.com.bancointer.app.mapper.EntityModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Elvis Fernandes on 28/06/19
 */
public abstract class AbstractEntityModelMapper<T, D> implements EntityModelMapper<T, D> {

    //@Autowired
    private final ModelMapper modelMapper;

    protected AbstractEntityModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public T convertoToEntity(D dto) {
        return this.modelMapper.map(dto, this.getEntityClass());
    }

    @Override
    public D convertToDto(T entity) {
        return this.modelMapper.map(entity, this.getDtoClass());
    }

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.createTypeMap(this.getEntityClass(), this.getDtoClass()).setPostConverter(this.toDtoConverter());
        modelMapper.createTypeMap(this.getDtoClass(), this.getEntityClass()).setPostConverter(this.toEntityConverter());
    }

    @Override
    public List<D> convertToDtoList(List<T> entityList) {
        return (List)entityList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<T> convertToEntityList(List<D> entityList) {
        return (List)entityList.stream().map(this::convertoToEntity).collect(Collectors.toList());
    }

    private Class<T> getEntityClass() {
        return ((Class) ((ParameterizedType) this.getClass().
                getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    private Class<D> getDtoClass() {
        return ((Class) ((ParameterizedType) this.getClass().
                getGenericSuperclass()).getActualTypeArguments()[1]);
    }
}
