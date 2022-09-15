package org.learn.t1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class Task1ApplicationSimple {
    @Value("${kafka.t11.name}")
    private String t11;
    @Value("${kafka.t12.name}")
    private String t12;

    public static void main(String[] args) {
        SpringApplication.run(Task1ApplicationSimple.class, args);
    }

//    @Bean
//    public KStream<String, String> kStreamSimple(StreamsBuilder kStreamBuilder) {
//        kStreamBuilder.stream(t11).to(t12);
//        return null;
//    }
}
