package com.example.demo.database.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

    @Bean
    public DynamoDbEnhancedClient dynamoDbClient() {
        DynamoDbClient dbClient = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                // The region is meaningless for local DynamoDb but required for client builder validation
                .region(Region.of("local"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("foo", "bar")))
                .build();

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dbClient)
                .build();
    }
}
