package com.example.demo.domain.table;

import com.example.demo.domain.TaskEntity;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DescribeTableEnhancedResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class EnhancedCreateTable {

  EnhancedCreateTable(DynamoDbEnhancedClient enhancedClient) {
    // Create a DynamoDbTable object
    DynamoDbTable<TaskEntity> taskEntityDynamoDbTable =
        enhancedClient.table(TaskEntity.TASK_TABLE_NAME, TableSchema.fromBean(TaskEntity.class));

    DescribeTableEnhancedResponse describeTableEnhancedResponse =
        taskEntityDynamoDbTable.describeTable();
    if (Objects.isNull(describeTableEnhancedResponse)
        || Objects.isNull(describeTableEnhancedResponse.table())) {

      // Create the table
      taskEntityDynamoDbTable.createTable(
          builder ->
              builder.provisionedThroughput(
                  b -> b.readCapacityUnits(10L).writeCapacityUnits(10L).build()));

      log.info("Waiting for table creation...");

      try (DynamoDbWaiter waiter = DynamoDbWaiter.create()) { // DynamoDbWaiter is Autocloseable
        ResponseOrException<DescribeTableResponse> response =
            waiter
                .waitUntilTableExists(
                    builder -> builder.tableName(TaskEntity.TASK_TABLE_NAME).build())
                .matched();
        DescribeTableResponse tableDescription =
            response
                .response()
                .orElseThrow(
                    () ->
                        new RuntimeException(
                            TaskEntity.TASK_TABLE_NAME + " table was not created."));
        // The actual error can be inspected in response.exception()
        log.info(tableDescription.table().tableName() + " was created.");
      }
    }
  }
}
