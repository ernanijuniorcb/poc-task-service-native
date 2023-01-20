package com.example.demo.web;

import com.example.demo.domain.TaskEntity;
import com.example.demo.dto.PagedTaskResponse;
import com.example.demo.schema.TaskTableSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DemoController {

    private final DynamoDbEnhancedClient dynamoDbConfig;

    @GetMapping
    public PagedTaskResponse get(@RequestParam(required = false, name = "pageSize") Integer pageSize,
                                 @RequestParam(required = false, name = "lastKey") String lastKey) {

        ScanEnhancedRequest.Builder scanEnhancedRequestBuilder = ScanEnhancedRequest.builder();
        DynamoDbTable<TaskEntity> table = dynamoDbConfig.table("local-cx-tasks-services", TaskTableSchema.taskEntityTableSchema);

            if (Objects.nonNull(pageSize) && pageSize > 0) {
                scanEnhancedRequestBuilder.limit(pageSize);
            }

            if (StringUtils.hasText(lastKey)) {
                scanEnhancedRequestBuilder.exclusiveStartKey(Map.of("eid", AttributeValue.builder().s(lastKey).build()));
            }

        PagedTaskResponse pagedTaskResponse = PagedTaskResponse.builder().tasks(new ArrayList<>()).build();
        PageIterable<TaskEntity> scan = table.scan(scanEnhancedRequestBuilder.build());
        scan.stream().limit(pageSize).forEach(
        taskEntityPage -> {
          if (Objects.nonNull(taskEntityPage) && Objects.nonNull(taskEntityPage.items()) && !taskEntityPage.items().isEmpty() ) {
            pagedTaskResponse.setLastKey(
                Optional.ofNullable(taskEntityPage.lastEvaluatedKey())
                    .map(stringAttributeValueMap -> stringAttributeValueMap.get("eid"))
                    .map(AttributeValue::s)
                    .orElse(null));
            pagedTaskResponse.getTasks().addAll(taskEntityPage.items());
          }
        });

        return pagedTaskResponse;
    }

    @PostMapping
    public void post(@RequestBody TaskEntity taskEntity) {

        DynamoDbTable<TaskEntity> table = dynamoDbConfig.table("local-cx-tasks-services", TaskTableSchema.taskEntityTableSchema);

        table.putItem(taskEntity);

    }
}
