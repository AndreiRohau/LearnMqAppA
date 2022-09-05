package org.learn.config;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.CustomConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class JmsConfig {
    @Value("${routing.key.header}")
    private String routingKeyHeader;
    @Value("${spring.cloud.stream.rabbit.bindings.queue2Sink-in-0.consumer.bindingRoutingKey}")
    private String failedMessageRoutingKey;

    @Bean
    public Consumer<Message<String>> queue2Sink() {
        return new CustomConsumer("FAILED_MSG_CONSUMER");
    }

}
