package org.learn.example;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.learn.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class RequestProcessor {
    private ActiveMQConnectionFactory connectionFactory;
    private MessageListener messageListener;
    @Value("${activemq.client.queue.name}")
    private String clientQueueName;
    @Value("${activemq.client.delivery.mode}")
    private int deliveryMode;
    @Value("${activemq.transacted}")
    private boolean transacted;
    @Value("${activemq.ackMode}")
    private int ackMode;

    @Autowired
    public RequestProcessor(ActiveMQConnectionFactory connectionFactory, MessageListener messageListener) throws JMSException {
        this.connectionFactory = connectionFactory;
        this.messageListener = messageListener;
    }

    public void processRequest(String message) throws JMSException {
        Session session = getSession();
        Destination temporaryQueue = session.createTemporaryQueue();

        setListener(session, temporaryQueue);

        TextMessage txtMessage = prepareMessage(session, message, temporaryQueue);

        send(session, txtMessage);
    }

    private Session getSession() throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection.createSession(transacted, ackMode);
    }

    private void setListener(Session session, Destination tempDest) throws JMSException {
        MessageConsumer responseConsumer = session.createConsumer(tempDest);
        responseConsumer.setMessageListener(messageListener);
    }

    private TextMessage prepareMessage(Session session, String message, Destination tempDest) throws JMSException {
        TextMessage txtMessage = session.createTextMessage();
        txtMessage.setText(message);
        txtMessage.setJMSReplyTo(tempDest);
        txtMessage.setJMSCorrelationID(Util.createRandomString());
        return txtMessage;
    }

    private void send(Session session, TextMessage txtMessage) throws JMSException {
        MessageProducer producer = session.createProducer(session.createQueue(clientQueueName));
        producer.setDeliveryMode(deliveryMode);
        producer.send(txtMessage);
    }
}
