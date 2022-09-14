package org.learn.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;


@Configuration
public class JmsConfig {
    @Bean
    public InfrastructureConfig infrastructureConfig(@Value("${spring.kafka.servers}") String kafkaUrl,
                                                     @Value("${kafka.topic1.name}") String topic1Name) {
        return new InfrastructureConfig(kafkaUrl, topic1Name);
    }

    @Bean
    public AdminClient kafkaAdminClient(InfrastructureConfig infrastructureConfig) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, infrastructureConfig.getKafkaUrl());
        return AdminClient.create(kafkaProperties);
    }

    @Bean
    public NewTopic topic1(InfrastructureConfig infrastructureConfig, AdminClient kafkaAdminClient,
                           @Value("${kafka.topic1.partition}") int topic1Partition,
                           @Value("${kafka.topic1.replication}") short topic1Replication) {
        final NewTopic newTopic = new NewTopic(infrastructureConfig.getTopicName(), topic1Partition, topic1Replication);
        try (kafkaAdminClient) {
            kafkaAdminClient.createTopics(Collections.singleton(newTopic)).all().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create a topic1", e);
        }
        return newTopic;
    }


}
