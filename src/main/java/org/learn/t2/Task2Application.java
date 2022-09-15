package org.learn.t2;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class Task2Application {
    public static final String STREAMS_SHUTDOWN_HOOK = "streams-shutdown-hook";
    @Value("${kafka.t11.name}")
    private String t11;
    @Value("${kafka.t12.name}")
    private String t12;
    @Value("${spring.kafka.streams.application-id}")
    private String appId;
    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String servers;
    @Value("${kafka.t20.name}")
    private String t20;

    public static void main(String[] args) {
        SpringApplication.run(Task2Application.class, args);
    }

    @Bean
    public Map<String, KStream<Integer, String>> branches(StreamsBuilder kStreamBuilder) {
        KStream<Integer, String> kStream = kStreamBuilder.stream(t20, Consumed.with(Serdes.Integer(), Serdes.String()));
        BranchedKStream<Integer, String> branchedKStream = kStream
                .flatMap((k, v) -> getKeyValues(v))
                .peek((k, v) -> println("tmp", k, v))
                .split(Named.as("words-"))
                .branch((key, value) -> value.length() >= 10, Branched.as("long"))
                .branch((key, value) -> value.length() < 10, Branched.as("short"));
        return branchedKStream.noDefaultBranch();
    }

    private Set<KeyValue<Integer, String>> getKeyValues(String value) {
        if (value.contains(" ")) {
            String[] words = value.split(" ");
            return Arrays.stream(words).map(word -> getPair(word)).collect(Collectors.toSet());
        } else {
            Set<KeyValue<Integer, String>> keyValues = new HashSet<>();
            keyValues.add(getPair(value));
            return keyValues;
        }
    }

    private KeyValue<Integer, String> getPair(String value) {
        return KeyValue.pair(value.length(), value);
    }

    @Bean
    public KStream<Integer, String> secondTaskStreams(Map<String, KStream<Integer, String>> branches) {
        KStream<Integer, String> merged =
                branches.get("words-long").filter((k,v) -> v.contains("a"))
                .merge(branches.get("words-short").filter((k,v) -> v.contains("a")))
                        .peek((k,v) -> println("FINAL peek HAS [A]", k, v));

        return merged;
    }

    private void println(String status, Integer k, String v) {
        System.out.println(status + " k=" + k + " v=" + v);
    }

}
