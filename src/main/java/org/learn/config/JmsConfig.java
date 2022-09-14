package org.learn.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class JmsConfig {
    @Bean
    public InfrastructureConfig infrastructureConfig(@Value("${spring.kafka.servers}") String kafkaUrl,
                                                     @Value("${kafka.topic1.name}") String topic1Name) {
        return new InfrastructureConfig(kafkaUrl, topic1Name);
    }

    @Bean
    public KafkaAdmin kafkaAdmin(InfrastructureConfig infrastructureConfig) {
        final Map<String, Object> props = new HashMap<>();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, infrastructureConfig.getKafkaUrl());
        return new KafkaAdmin(props);
    }

    @Bean
    public NewTopic topic1(InfrastructureConfig infrastructureConfig, KafkaAdmin kafkaAdmin,
                           @Value("${kafka.topic1.partition}") int topic1Partition,
                           @Value("${kafka.topic1.replication}") short topic1Replication) {
        return new NewTopic(infrastructureConfig.getTopicName(), topic1Partition, topic1Replication);
    }

    @Bean
    public ProducerFactory<String, Msg> producerFactory(InfrastructureConfig infrastructureConfig) {
        final Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, infrastructureConfig.getKafkaUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MsgSerializer.class);
//        props.put(ProducerConfig.ACKS_CONFIG, "all");
        return new DefaultKafkaProducerFactory<>(props);
    }

//    @Bean
//    public ReplyingKafkaTemplate<String, Msg, Msg> replyingKafkaTemplate(@Value("${kafka.topic2.name}") String topic2Name,
//                                                                         @Value("${kafka.topic1.consumer.group.id}") String consumerGroupId,
//                                                                         ProducerFactory<String, Msg> producerFactory,
//                                                                         ConcurrentKafkaListenerContainerFactory<String, Msg> listenerFactory) {
//        ConcurrentMessageListenerContainer<String, Msg> replyContainer = listenerFactory.createContainer(topic2Name);
//        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
//        replyContainer.getContainerProperties().setGroupId(consumerGroupId);
//        return new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
//    }

    @Bean
    public KafkaTemplate<String, Msg> kafkaTemplate(ProducerFactory<String, Msg> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory(InfrastructureConfig infrastructureConfig,
                                                           @Value("${kafka.topic1.consumer.group.id}") String consumerGroupId) {
        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, infrastructureConfig.getKafkaUrl());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MsgDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        return kafkaListenerContainerFactory;
    }
}
