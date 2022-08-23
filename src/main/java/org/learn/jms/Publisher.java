package org.learn.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    private JmsTemplate jmsTopicTemplate;
    @Value("${activemq.topic}")
    private String topic;

    @Autowired
    public Publisher(JmsTemplate jmsTopicTemplate) {
        this.jmsTopicTemplate = jmsTopicTemplate;
    }

    public void createTopic(String message) {
        jmsTopicTemplate.convertAndSend(topic, message);
    }
}
