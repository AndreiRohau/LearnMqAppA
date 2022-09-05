package org.learn.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.learn.jms.CustomConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class JmsConfig {
    @Bean
    public Consumer<Message<String>> failedSink() {
        System.out.println("FAILED!!!! failedSink() ");
        return new CustomConsumer("FAILED_MSG_SERVICE");

    }

    @Bean
    public Consumer<Message<String>> deadletterSink() {
        return in -> System.out.println("DEADLETTER!!!! " + in.getPayload());
    }

    @SneakyThrows
    private void writeToFile(String messageText) {
        Path path = Paths.get("C:\\Users\\Andrei_Rohau\\IdeaProjects\\LearnMqAppA\\common.txt");
        Files.write(path, (messageText).getBytes());
    }

}
