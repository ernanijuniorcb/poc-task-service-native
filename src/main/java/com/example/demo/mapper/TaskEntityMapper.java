package com.example.demo.mapper;

import com.example.demo.domain.TaskEntity;
import com.example.demo.dto.TaskEntityDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskEntityMapper {

  TaskEntityMapper INSTANCE = Mappers.getMapper(TaskEntityMapper.class);

  TaskEntityDto toDto(TaskEntity taskEntity);

  TaskEntity toEntity(TaskEntityDto dto);

  List<TaskEntity> toEntityList(List<TaskEntityDto> taskEntityDtoList);

  List<TaskEntityDto> toDtoList(List<TaskEntity> taskEntities);
}
