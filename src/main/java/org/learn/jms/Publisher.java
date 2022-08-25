package org.learn.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static org.learn.jms.Destination.TOPIC;

@Component
public class Publisher implements Sendable {
    @Value("${activemq.topic}")
    private String topic;
    private final JmsTemplate jmsTopicTemplate;

    @Autowired
    public Publisher(JmsTemplate jmsTopicTemplate) {
        this.jmsTopicTemplate = jmsTopicTemplate;
    }

    @Override
    public void sendMessage(String message, boolean isPersistent) {
        jmsTopicTemplate.setDeliveryPersistent(isPersistent);
        jmsTopicTemplate.convertAndSend(topic, message);
        TOPIC.riseEmitted();
    }
}
