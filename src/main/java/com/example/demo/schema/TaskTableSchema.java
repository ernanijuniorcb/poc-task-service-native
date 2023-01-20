package com.example.demo.schema;

import com.example.demo.domain.TaskEntity;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primaryPartitionKey;

public class TaskTableSchema {

  public static final TableSchema<TaskEntity> taskEntityTableSchema =
      TableSchema.builder(TaskEntity.class)
          .newItemSupplier(TaskEntity::new)
          .addAttribute(
              String.class,
              a ->
                  a.name("eid")
                      .getter(TaskEntity::getEid)
                      .setter(TaskEntity::setEid)
                      .tags(primaryPartitionKey()))
          .addAttribute(
              String.class,
              a ->
                  a.name("outletId")
                      .getter(TaskEntity::getOutletId)
                      .setter(TaskEntity::setOutletId))
          .addAttribute(
              String.class,
              a ->
                  a.name("assignedTo")
                      .getter(TaskEntity::getAssignedTo)
                      .setter(TaskEntity::setAssignedTo))
          .addAttribute(
              String.class,
              a ->
                  a.name("dueDateTime")
                      .getter(TaskEntity::getDueDateTime)
                      .setter(TaskEntity::setDueDateTime))
          .build();
}
