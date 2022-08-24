package org.learn.jms;

import javax.jms.JMSException;

public interface Sendable {
    void sendMessage(String message, boolean isPersistent) throws JMSException;
}
