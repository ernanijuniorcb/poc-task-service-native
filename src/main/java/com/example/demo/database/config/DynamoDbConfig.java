package com.example.demo.database.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration(proxyBeanMethods = false)
public class DynamoDbConfig {

  @Value("${aws.default.region}")
  public String awsRegion;

  @Bean
  public DynamoDbEnhancedClient dynamoDbClient() {
    DynamoDbClient dbClient =
        DynamoDbClient.builder()
            //            .endpointOverride(URI.create("http://localhost:8000"))
            // The region is meaningless for local DynamoDb but required for client builder
            // validation
            .region(Region.of(awsRegion))
            //            .credentialsProvider(
            //                StaticCredentialsProvider.create(AwsBasicCredentials.create("foo",
            // "bar")))
            .build();

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dbClient)
                .build();
    }
}
