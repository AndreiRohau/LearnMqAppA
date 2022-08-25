package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static org.learn.jms.Destination.TOPIC;

@Slf4j
@Component
public class Publisher {
    @Value("VirtualTopic.${activemq.virtual.topic}")
    private String topicName;
    private final JmsTemplate jmsTopicTemplate;

    @Autowired
    public Publisher(JmsTemplate jmsTopicTemplate) {
        this.jmsTopicTemplate = jmsTopicTemplate;
    }

    public void sendMessage(String message, boolean isPersistent) {
        jmsTopicTemplate.setDeliveryPersistent(isPersistent);
        jmsTopicTemplate.convertAndSend(new ActiveMQTopic(topicName), message);
        TOPIC.riseEmitted();
        TOPIC.append(TOPIC.getEmitted() + ". " + message);
    }
}
