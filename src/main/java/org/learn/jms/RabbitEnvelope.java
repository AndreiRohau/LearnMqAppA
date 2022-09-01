package org.learn.jms;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.MessageProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RabbitEnvelope {
    private String message;
    private String exchange;
    private String queue;
    private String routingKey;
    private boolean isMandatory;
    private AMQP.BasicProperties properties = MessageProperties.PERSISTENT_TEXT_PLAIN;
}
