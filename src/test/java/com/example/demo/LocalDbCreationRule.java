package com.example.demo;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

public class LocalDbCreationRule implements BeforeAllCallback, AfterAllCallback {
  static final GenericContainer<?> dynamoDBLocal =
      new FixedHostPortGenericContainer<>("amazon/dynamodb-local")
          .withCommand("-jar DynamoDBLocal.jar -inMemory -sharedDb")
          .withExposedPorts(8000)
          .withEnv("AWS_ACCESS_KEY_ID", "FAKE")
          .withEnv("AWS_SECRET_ACCESS_KEY", "FAKE")
          .withFixedExposedPort(8000, 8000);

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    dynamoDBLocal.start();
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    dynamoDBLocal.stop();
  }
}
