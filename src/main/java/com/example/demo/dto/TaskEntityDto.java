package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskEntityDto {

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
}
