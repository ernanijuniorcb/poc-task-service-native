package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDbBean
public class TaskEntity {

  public static final String TASK_TABLE_NAME = "TaskServices";
  private String eid;
  private String dueDateTime;
  private String title;
  private String description;
  private String outletId;
  private String outletName;
  private String salesOrg;
  private String assignedTo;
  private boolean done;
  private String createdBy;
  private String modifiedBy;
  private String createdAt;
  private String modifiedAt;
  private String createdByApp;
  private String modifiedByApp;

  @DynamoDbPartitionKey
  public String getEid() {
    return eid;
  }
}
