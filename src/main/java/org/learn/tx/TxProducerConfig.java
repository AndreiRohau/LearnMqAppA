package org.learn.tx;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import java.util.stream.Stream;


public class TxProducerConfig {

    @Bean
    public Runnable produce(KafkaProducer<String, String> kafkaProducer) {
        return () -> {
            try {
                kafkaProducer.beginTransaction();
                final String DATA_MESSAGE_1 = "Put any space separated data here for count";
                final String DATA_MESSAGE_2 = "Output will contain count of every word in the message";
                Stream.of(DATA_MESSAGE_1, DATA_MESSAGE_2)
                        .forEach(s -> kafkaProducer.send(new ProducerRecord<String, String>("input", null, s)));
                kafkaProducer.commitTransaction();
            } catch (KafkaException e) {
                kafkaProducer.abortTransaction();
            }
        };
    }

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "prod-0");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
        kafkaProducer.initTransactions();
        return kafkaProducer;

    }
}
