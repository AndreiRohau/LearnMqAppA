package org.learn.t3_51_21;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;

import static java.util.Objects.nonNull;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class Task3Application {
    public static final String ANY_SEPARATOR = "∞∞∞";
    @Value("${spring.kafka.streams.application-id}")
    private String appId;
    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String servers;
    @Value("${kafka.t31.name}")
    private String t31;
    @Value("${kafka.t32.name}")
    private String t32;

    public static void main(String[] args) {
        SpringApplication.run(Task3Application.class, args);
    }

    @Bean
    public KStream<Long, String> kStreamT31(StreamsBuilder kStreamBuilder) {
        return firstStepProcessing(kStreamBuilder, t31);
    }

    @Bean
    public KStream<Long, String> kStreamT32(StreamsBuilder kStreamBuilder) {
        return firstStepProcessing(kStreamBuilder, t32);
    }

    private KStream<Long, String> firstStepProcessing(StreamsBuilder kStreamBuilder, @Value("${kafka.t31.name}") String topic) {
        return kStreamBuilder
                .stream(topic, Consumed.with(Serdes.Integer(), Serdes.String()))
                .filter((k, v) -> nonNull(v) && v.contains(":"))
                .map((k, v) -> KeyValue.pair(Long.parseLong(v.split(":")[0]), v))
                .peek(print("PEEK"));
    }

    @Bean
    public KStream<Long, String> joined(KStream<Long, String> kStreamT31, KStream<Long, String> kStreamT32) {
        ValueJoiner<String, String, String> valueJoiner = (v1, v2) -> v1 + ANY_SEPARATOR + v2;
        return kStreamT31
                .join(
                        kStreamT32,
                        valueJoiner,
                        JoinWindows.ofTimeDifferenceAndGrace(Duration.ofSeconds(60), Duration.ofSeconds(30)),
                        StreamJoined.with(Serdes.Long(), Serdes.String(), Serdes.String()))
                .peek(print("JOINED"));
    }

    private ForeachAction<Long, String> print(final String status) {
        return (k, v) -> System.out.println(status + ": KEY = " + k + ", VALUE = " + v);
    }

}
