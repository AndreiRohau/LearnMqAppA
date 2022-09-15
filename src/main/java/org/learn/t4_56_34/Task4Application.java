package org.learn.t4_56_34;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import static java.util.Objects.nonNull;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class Task4Application {
    @Value("${spring.kafka.streams.application-id}")
    private String appId;
    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String servers;
    @Value("${kafka.t40.name}")
    private String t40;

    public static void main(String[] args) {
        SpringApplication.run(Task4Application.class, args);
    }

// {"name":"John","company":"EPAM","position":"developer","experience":5}
    @Bean
    public KStream<Long, User> kStreamT40(StreamsBuilder kStreamBuilder) {
        return kStreamBuilder
                .stream(t40, Consumed.with(Serdes.Long(), Serdes.serdeFrom(new UserSerializer(), new UserDeserializer())))
                .filter((k, v) -> nonNull(v))
                .peek(print("RECEIVED"));
    }

    private ForeachAction<Long, User> print(final String status) {
        return (k, v) -> System.out.println(status + ": KEY = " + k + ", VALUE = " + v);
    }

}
