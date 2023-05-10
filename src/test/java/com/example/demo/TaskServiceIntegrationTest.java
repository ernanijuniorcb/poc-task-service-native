package com.example.demo;

import com.example.demo.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@ExtendWith({LocalDbCreationRule.class})
@SpringBootTest
class TaskServiceIntegrationTest {

  @Autowired TaskService taskService;

  @Autowired DynamoDbEnhancedClient dynamoDbConfig;

  void test1() {
    Assertions.assertEquals(1, 1);
  }
}
