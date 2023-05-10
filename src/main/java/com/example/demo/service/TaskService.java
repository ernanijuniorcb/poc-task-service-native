package com.example.demo.service;

import com.example.demo.domain.TaskEntity;
import com.example.demo.dto.PagedTaskResponseDto;
import com.example.demo.dto.TaskEntityDto;
import com.example.demo.exception.InvalidEidException;
import com.example.demo.exception.InvalidTaskException;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.mapper.TaskEntityMapper;
import com.example.demo.schema.TaskTableSchema;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Service
@RequiredArgsConstructor
public class TaskService {

  private static final String AND_TOKEN = " AND ";
  private final DynamoDbEnhancedClient dynamoDbConfig;

  private final ScanEnhancedRequest.Builder scanEnhancedRequestBuilder =
      ScanEnhancedRequest.builder();
  private DynamoDbTable<TaskEntity> table;

  @PostConstruct
  private void init() {
    table = dynamoDbConfig.table(TaskEntity.TASK_TABLE_NAME, TaskTableSchema.taskEntityTableSchema);
  }

  public PagedTaskResponseDto getAllTasksBy(
      final String outletId,
      final Boolean done,
      final String title,
      final String assignedTo,
      final Integer pageSize,
      final String lastKey) {

    if (Objects.nonNull(pageSize) && pageSize > 0) {
      scanEnhancedRequestBuilder.limit(pageSize);
    }

    if (StringUtils.hasText(lastKey)) {
      scanEnhancedRequestBuilder.exclusiveStartKey(
          Map.of("eid", AttributeValue.builder().s(lastKey).build()));
    }

    Expression expressionOutlet = null;
    if (StringUtils.hasText(outletId)) {
      expressionOutlet =
          Expression.builder()
              .putExpressionName("#outletId", "outletId")
              .putExpressionValue(":val1", AttributeValue.builder().s(outletId).build())
              .expression("#outletId = :val1")
              .build();
    }

    Expression expressionDone = null;
    if (Objects.nonNull(done)) {
      expressionDone =
          Expression.builder()
              .putExpressionName("#done", "done")
              .putExpressionValue(":val2", AttributeValue.builder().bool(done).build())
              .expression("#done = :val2")
              .build();
    }

    Expression expressionTitle = null;
    if (StringUtils.hasText(title)) {
      expressionTitle =
          Expression.builder()
              .putExpressionName("#title", "title")
              .putExpressionValue(":val3", AttributeValue.builder().s(outletId).build())
              .expression("#title = :val3")
              .build();
    }

    Expression expressionAssignedTo = null;
    if (StringUtils.hasText(assignedTo)) {
      expressionAssignedTo =
          Expression.builder()
              .putExpressionName("#assignedTo", "assignedTo")
              .putExpressionValue(":val4", AttributeValue.builder().s(outletId).build())
              .expression("#assignedTo = :val4")
              .build();
    }

    Expression expression =
        Expression.join(expressionAssignedTo, expressionDone, AND_TOKEN)
            .and(expressionOutlet)
            .and(expressionTitle);

    return getPagedTaskResponseDto(pageSize, expression);
  }

  public TaskEntityDto createTask(final TaskEntityDto taskEntityDto) {
    if (validateCreate(taskEntityDto) && validateUpdate(taskEntityDto)) {

      table.putItem(TaskEntityMapper.INSTANCE.toEntity(taskEntityDto));
      return taskEntityDto;
    } else {
      throw new InvalidTaskException();
    }
  }

  public TaskEntityDto getTaskByEid(final String eid) {
    TaskEntity taskEntity = table.getItem(Key.builder().partitionValue(eid).build());
    if (Objects.isNull(taskEntity)) {
      throw new TaskNotFoundException(String.format("Task with id %s not found", eid));
    }
    return TaskEntityMapper.INSTANCE.toDto(taskEntity);
  }

  public TaskEntityDto updateTask(final String eid, final TaskEntityDto taskEntityDto) {
    if (validateUpdate(taskEntityDto)) {
      TaskEntityDto updateTaskDto = getTaskByEid(eid);
      BeanUtils.copyProperties(taskEntityDto, updateTaskDto);
      table.updateItem(TaskEntityMapper.INSTANCE.toEntity(updateTaskDto));
      return taskEntityDto;
    } else {
      throw new InvalidTaskException();
    }
  }

  public void deleteTask(final String eid) {
    if (StringUtils.hasText(eid)) {
      TaskEntityDto taskByEid = getTaskByEid(eid);
      table.deleteItem(TaskEntityMapper.INSTANCE.toEntity(taskByEid));
    } else {
      throw new InvalidEidException("Eid can't be empty");
    }
  }

  public PagedTaskResponseDto getAllTasksWithoutOutletName(
      final Integer pageSize, final String lastKey) {

    if (Objects.nonNull(pageSize) && pageSize > 0) {
      scanEnhancedRequestBuilder.limit(pageSize);
    }

    if (StringUtils.hasText(lastKey)) {
      scanEnhancedRequestBuilder.exclusiveStartKey(
          Map.of("eid", AttributeValue.builder().s(lastKey).build()));
    }

    Expression expressionOutlet =
        Expression.builder()
            .putExpressionName("#outletName", "outletName")
            .putExpressionValue(":val1", AttributeValue.builder().nul(true).build())
            .expression("#outletId = :val1")
            .build();

    return getPagedTaskResponseDto(pageSize, expressionOutlet);
  }

  private PagedTaskResponseDto getPagedTaskResponseDto(
      Integer pageSize, Expression expressionOutlet) {
    PagedTaskResponseDto pagedTaskResponse =
        PagedTaskResponseDto.builder().tasks(new ArrayList<>()).build();
    PageIterable<TaskEntity> scan =
        table.scan(scanEnhancedRequestBuilder.filterExpression(expressionOutlet).build());
    scan.stream()
        .limit(pageSize)
        .forEach(
            taskEntityPage -> {
              if (Objects.nonNull(taskEntityPage)
                  && Objects.nonNull(taskEntityPage.items())
                  && !taskEntityPage.items().isEmpty()) {
                pagedTaskResponse.setStartIndex(
                    Optional.ofNullable(taskEntityPage.lastEvaluatedKey())
                        .map(stringAttributeValueMap -> stringAttributeValueMap.get("eid"))
                        .map(AttributeValue::s)
                        .orElse(null));
                pagedTaskResponse
                    .getTasks()
                    .addAll(TaskEntityMapper.INSTANCE.toDtoList(taskEntityPage.items()));
              }
            });
    return pagedTaskResponse;
  }

  private boolean validateCreate(final TaskEntityDto taskEntityDto) {
    return Stream.<Predicate<TaskEntityDto>>of(
            Objects::nonNull, task -> StringUtils.hasText(task.getCreatedByApp()))
        .allMatch(v -> v.test(taskEntityDto));
  }

  private boolean validateUpdate(final TaskEntityDto taskEntityDto) {
    return Stream.<Predicate<TaskEntityDto>>of(
            Objects::nonNull,
            task -> StringUtils.hasText(task.getTitle()),
            task -> StringUtils.hasText(task.getDueDateTime()),
            task -> StringUtils.hasText(task.getSalesOrg()),
            task -> StringUtils.hasText(task.getAssignedTo()),
            task -> StringUtils.hasText(task.getCreatedBy()),
            task -> StringUtils.hasText(task.getModifiedByApp()))
        .allMatch(v -> v.test(taskEntityDto));
  }
}
