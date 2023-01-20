package com.example.demo.dto;

import com.example.demo.domain.TaskEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagedTaskResponse {

    List<TaskEntity> tasks;
    String lastKey;
}
