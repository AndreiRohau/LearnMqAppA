package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import static org.learn.jms.Destination.QUEUE;

@Slf4j
@Component
public class QueueResponseListener {
    @Value("${activemq.response.queue}")
    private String responseQueueName;

    @Autowired
    private JmsTemplate jmsQueueTemplate;

    @JmsListener(destination = "${activemq.response.queue}", containerFactory = "queueResponseListenerFactory")
    public void receiveMessageFromQueue(Message message) throws JMSException, InterruptedException {
        TextMessage textMessage = (TextMessage) message;
        String messageData = textMessage.getText();
        log.info("Received message: " + messageData + ". From queue: " + responseQueueName);
        QUEUE.riseProcessed();
        QUEUE.append(QUEUE.getProcessed() + ". " + messageData);
    }
}
