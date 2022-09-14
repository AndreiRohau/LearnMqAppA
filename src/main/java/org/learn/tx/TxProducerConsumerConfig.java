package org.learn.tx;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.Duration.ofSeconds;
import static java.util.Collections.singleton;


public class TxProducerConsumerConfig {

    private static final String CONSUMER_GROUP_ID = "my-group-id";
    private static final String OUTPUT_TOPIC = "output";
    private static final String INPUT_TOPIC = "input";

    @Bean
    public Runnable produceTx(KafkaProducer<String, String> kafkaProducerTx, KafkaConsumer<String, String> kafkaConsumerTx) {
        return () -> {
            kafkaProducerTx.initTransactions();
            try {
                while (true) {
                    ConsumerRecords<String, String> records = kafkaConsumerTx.poll(ofSeconds(60));
                    Map<String, Integer> wordCountMap = records.records(new TopicPartition(INPUT_TOPIC, 0))
                            .stream()
                            .flatMap(record -> Stream.of(record.value()
                                    .split(" ")))
                            .map(word -> Tuple.of(word, 1))
                            .collect(Collectors.toMap(Tuple::getKey, Tuple::getValue, Integer::sum));
                    kafkaProducerTx.beginTransaction();
                    wordCountMap.forEach((key, value) -> kafkaProducerTx.send(new ProducerRecord<String, String>(OUTPUT_TOPIC, key, value.toString())));
                    Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();
                    for (TopicPartition partition : records.partitions()) {
                        List<ConsumerRecord<String, String>> partitionedRecords = records.records(partition);
                        long offset = partitionedRecords.get(partitionedRecords.size() - 1)
                                .offset();
                        offsetsToCommit.put(partition, new OffsetAndMetadata(offset + 1));
                    }
                    kafkaProducerTx.sendOffsetsToTransaction(offsetsToCommit, CONSUMER_GROUP_ID);
                    kafkaProducerTx.commitTransaction();
                }
            } catch (KafkaException e) {
                kafkaProducerTx.abortTransaction();
            }
        };
    }

    @Bean
    public KafkaConsumer<String, String> kafkaConsumerTx() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(singleton(INPUT_TOPIC));
        return consumer;
    }

    @Bean
    public KafkaProducer<String, String> kafkaProducerTx() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "prod-1");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer(props);
    }
}
