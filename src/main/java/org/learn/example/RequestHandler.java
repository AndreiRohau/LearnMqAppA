package org.learn.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
@Slf4j
public class RequestHandler implements MessageListener {
    @Override
    public void onMessage(Message message) {
        log.info("RequestHandler WORKED!!!");
        String messageText = null;
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                messageText = textMessage.getText();
                log.info("#onMessage: messageText = " + messageText);
            }
        } catch (JMSException e) {
            //Handle the exception appropriately
        }
    }
}
