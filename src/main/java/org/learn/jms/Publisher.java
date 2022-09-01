package org.learn.jms;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class Publisher {
    @Autowired
    private Connection connection;

    public void publishEvent(RabbitEnvelope re) {
        try (Channel channel = connection.createChannel()) {

            // publish messages
            channel.basicPublish(re.getExchange(),
                    re.getRoutingKey(),
                    re.isMandatory(),
                    re.getProperties(),
                    re.getMessage().getBytes(StandardCharsets.UTF_8));

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
