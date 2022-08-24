package org.learn.jms;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Publisher implements Sendable {
    private JmsTemplate jmsTopicTemplate;
    @Value("${activemq.topic}")
    private String topicName;

    @Autowired
    public Publisher(JmsTemplate jmsTopicTemplate) {
        this.jmsTopicTemplate = jmsTopicTemplate;
    }

    @Override
    public void sendMessage(String message, boolean isPersistent) {
        jmsTopicTemplate.setDeliveryPersistent(isPersistent);
        jmsTopicTemplate.convertAndSend(new ActiveMQTopic(topicName), message);
    }
}
