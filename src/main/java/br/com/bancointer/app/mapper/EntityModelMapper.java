package br.com.bancointer.app.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.List;

/**
 * @author Elvis Fernandes on 28/06/19
 */
public interface EntityModelMapper<T, D> {

    //Class<T> getEntityClass();

    //Class<D> getDtoClass();

    Converter<T, D> toDtoConverter();

    Converter<D, T> toEntityConverter();

    void configure(ModelMapper modelMapper);

    T convertoToEntity(D dto);

    D convertToDto(T entity);

    List<D> convertToDtoList(List<T> entityList);

    List<T> convertToEntityList(List<D> entityList);
}
