package com.example.demo.schema;

import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primaryPartitionKey;

import com.example.demo.domain.TaskEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
              a -> a.name("title").getter(TaskEntity::getTitle).setter(TaskEntity::setTitle))
          .addAttribute(
              String.class,
              a ->
                  a.name("description")
                      .getter(TaskEntity::getDescription)
                      .setter(TaskEntity::setDescription))
          .addAttribute(
              String.class,
              a ->
                  a.name("outletName")
                      .getter(TaskEntity::getOutletName)
                      .setter(TaskEntity::setOutletName))
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
          .addAttribute(
              String.class,
              a ->
                  a.name("salesOrg")
                      .getter(TaskEntity::getSalesOrg)
                      .setter(TaskEntity::setSalesOrg))
          .addAttribute(
              boolean.class,
              a -> a.name("done").getter(TaskEntity::isDone).setter(TaskEntity::setDone))
          .addAttribute(
              String.class,
              a ->
                  a.name("createdBy")
                      .getter(TaskEntity::getCreatedBy)
                      .setter(TaskEntity::setCreatedBy))
          .addAttribute(
              String.class,
              a ->
                  a.name("modifiedBy")
                      .getter(TaskEntity::getModifiedBy)
                      .setter(TaskEntity::setModifiedBy))
          .addAttribute(
              String.class,
              a ->
                  a.name("createdAt")
                      .getter(TaskEntity::getCreatedAt)
                      .setter(TaskEntity::setCreatedAt))
          .addAttribute(
              String.class,
              a ->
                  a.name("modifiedAt")
                      .getter(TaskEntity::getModifiedAt)
                      .setter(TaskEntity::setModifiedAt))
          .addAttribute(
              String.class,
              a ->
                  a.name("createdByApp")
                      .getter(TaskEntity::getCreatedByApp)
                      .setter(TaskEntity::setCreatedByApp))
          .addAttribute(
              String.class,
              a ->
                  a.name("modifiedByApp")
                      .getter(TaskEntity::getModifiedByApp)
                      .setter(TaskEntity::setModifiedByApp))
          .build();
}
