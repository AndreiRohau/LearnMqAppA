package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import static org.learn.jms.Destination.QUEUE;

@Slf4j
@Component
public class Producer implements Sendable {

    private JmsTemplate jmsQueueTemplate;
    @Value("${activemq.queue}")
    private String queue;
    @Value("${activemq.response.queue}")
    private String responseQueueName;

    @Autowired
    public Producer(JmsTemplate jmsQueueTemplate) {
        this.jmsQueueTemplate = jmsQueueTemplate;
    }

    @Override
    public void sendMessage(String message, boolean isPersistent) throws JMSException {
        jmsQueueTemplate.setDeliveryPersistent(isPersistent);
        jmsQueueTemplate.convertAndSend(queue, message);
        QUEUE.riseEmitted();
        String response = (String) jmsQueueTemplate.receiveAndConvert(responseQueueName);
        String messageData = response;
        log.info("Received message: " + messageData + ". From queue: " + responseQueueName);
        QUEUE.riseProcessed();
        QUEUE.append(QUEUE.getProcessed() + ". " + messageData);
    }
}
