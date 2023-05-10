package com.example.demo.web;

import com.example.demo.domain.TaskEntity;
import com.example.demo.dto.PagedTaskResponseDto;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DemoController {

  private final TaskService taskService;

  @GetMapping
  public PagedTaskResponseDto getAllTasksBy(
      @RequestParam(required = false, name = "outletId") final String outletId,
      @RequestParam(required = false, name = "done") final Boolean done,
      @RequestParam(required = false, name = "title") final String title,
      @RequestParam(required = false, name = "assignedTo") final String assignedTo,
      @RequestParam(required = false, name = "pageSize") Integer pageSize,
      @RequestParam(required = false, name = "lastKey") String lastKey) {
    return taskService.getAllTasksBy(outletId, done, title, assignedTo, pageSize, lastKey);
  }

  @PostMapping
  public void post(@RequestBody TaskEntity taskEntity) {

    //    DynamoDbTable<TaskEntity> table =
    //        dynamoDbConfig.table("TaskServices", TaskTableSchema.taskEntityTableSchema);
    //
    //    table.putItem(taskEntity);
  }
}
