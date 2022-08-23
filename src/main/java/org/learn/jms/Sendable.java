package org.learn.jms;

public interface Sendable {
    void sendMessage(String message, boolean isPersistent);
}
