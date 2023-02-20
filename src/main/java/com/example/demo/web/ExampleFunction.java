package com.example.demo.web;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.demo.domain.TaskEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.function.Function;

@Component
@Slf4j
@Validated
public class ExampleFunction
    implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private final ObjectMapper objectMapper;

  public ExampleFunction(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * Lambda function handler that takes a request and returns a response.
   *
   * @param proxyRequestEvent the function argument
   * @return {@link APIGatewayProxyResponseEvent}
   * @throws JsonProcessingException
   */
  @Override
  @SneakyThrows(value = JsonProcessingException.class)
  public APIGatewayProxyResponseEvent apply(final APIGatewayProxyRequestEvent proxyRequestEvent) {
    log.info("Converting request into a response...'");

    final TaskEntity request =
        objectMapper.readValue(proxyRequestEvent.getBody(), TaskEntity.class);

    final TaskEntity response = TaskEntity.builder().eid(request.getEid()).build();

    log.info("Converted request into a response.");

    return new APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withBody(objectMapper.writeValueAsString(response));
  }
}
