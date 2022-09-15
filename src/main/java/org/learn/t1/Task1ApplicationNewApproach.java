package org.learn.t1;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class Task1ApplicationNewApproach {
    public static final String STREAMS_SHUTDOWN_HOOK = "streams-shutdown-hook";
    @Value("${kafka.t11.name}")
    private String t11;
    @Value("${kafka.t12.name}")
    private String t12;
    @Value("${spring.kafka.streams.application-id}")
    private String appId;
    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String servers;

    public static void main(String[] args) {
        SpringApplication.run(Task1ApplicationNewApproach.class, args);
    }

    @Bean
    public KStream<String, String> kStreamTask1(StreamsBuilder kStreamBuilder, Properties propertiesTask1) {
        KStream<String, String> kStreamTask1 = kStreamBuilder.stream(t11, Consumed.with(Serdes.String(), Serdes.String()));
        kStreamTask1.to(t12);
        final Topology topology = kStreamBuilder.build();
        final KafkaStreams streams = new KafkaStreams(topology, propertiesTask1);
        Runtime.getRuntime().addShutdownHook(new Thread(STREAMS_SHUTDOWN_HOOK) {
            @Override
            public void run() {
                streams.close();
            }
        });
        return kStreamTask1;
    }

    @Bean
    public Properties propertiesTask1() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }



}
