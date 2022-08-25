package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import static org.learn.jms.Destination.QUEUE;

@Slf4j
@Component
public class Producer {
    @Value("${activemq.queue}")
    private String queue;
    private final JmsTemplate jmsQueueTemplate;

    @Autowired
    public Producer(JmsTemplate jmsQueueTemplate) {
        this.jmsQueueTemplate = jmsQueueTemplate;
    }

    public void sendMessage(String message, boolean isPersistent) throws JMSException {
        QUEUE.riseEmitted();
        jmsQueueTemplate.setDeliveryPersistent(isPersistent);
        Message rawMessage = jmsQueueTemplate.sendAndReceive(queue, prepareMessageCreator(message));
        TextMessage textMessage = (TextMessage) rawMessage;
        String stringMessage = textMessage.getText();
        log.info("RECEIVED==={}. Q={}.", stringMessage, textMessage.getJMSDestination());
        QUEUE.riseProcessed();
        QUEUE.append(QUEUE.getProcessed() + ". " + stringMessage);
    }

    private MessageCreator prepareMessageCreator(final String message) {
        return session -> {
            TextMessage msg = session.createTextMessage();
            msg.setText(message);
            msg.setJMSReplyTo(session.createTemporaryQueue()); // NB! TEMPORARY QUEUE = no names
            log.info("SENDING==={}. CorrId={}, dest_Q={}, reply_Q={}.", msg.getText(), msg.getJMSCorrelationID(), msg.getJMSDestination(),msg.getJMSReplyTo());
            return msg;
        };
    }
}
