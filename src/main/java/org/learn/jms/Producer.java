package org.learn.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static org.learn.jms.Destination.QUEUE;

@Component
public class Producer implements Sendable {
    @Value("${activemq.queue}")
    private String queue;
    private final JmsTemplate jmsQueueTemplate;

    @Autowired
    public Producer(JmsTemplate jmsQueueTemplate) {
        this.jmsQueueTemplate = jmsQueueTemplate;
    }

    @Override
    public void sendMessage(String message, boolean isPersistent) {
        jmsQueueTemplate.setDeliveryPersistent(isPersistent);
        jmsQueueTemplate.convertAndSend(queue, message);
        QUEUE.riseEmitted();
    }
}
