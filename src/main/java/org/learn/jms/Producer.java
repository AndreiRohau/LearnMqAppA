package org.learn.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private JmsTemplate jmsQueueTemplate;
    @Value("${activemq.queue}")
    private String queue;

    @Autowired
    public Producer(JmsTemplate jmsQueueTemplate) {
        this.jmsQueueTemplate = jmsQueueTemplate;
    }

    public void sendMessage(String message) {
        jmsQueueTemplate.convertAndSend(queue, message);
    }
}
