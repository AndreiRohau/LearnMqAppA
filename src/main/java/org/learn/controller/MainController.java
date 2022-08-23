package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;

    private JmsTemplate jmsQueueTemplate;
    private Queue jmsQueue;
    private JmsTemplate jmsTopicTemplate;
    private Topic jmsTopic;

    @Autowired
    public MainController(JmsTemplate jmsQueueTemplate, Queue jmsQueue, JmsTemplate jmsTopicTemplate, Topic jmsTopic) {
        this.jmsQueueTemplate = jmsQueueTemplate;
        this.jmsQueue = jmsQueue;
        this.jmsTopicTemplate = jmsTopicTemplate;
        this.jmsTopic = jmsTopic;
    }
    @GetMapping("/status")
    public String status() {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Working!";
        log.info(logMessage);
        return logMessage;
    }

    @GetMapping("/")
    public String index() {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "index!";
        log.info(logMessage);
        return logMessage;
    }

    @GetMapping("/queue/{message}")
    public String sendMessageToQueue(@PathVariable String message) {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Message to queue=[" + message + "].";
        log.info(logMessage);
        jmsQueueTemplate.send(jmsQueue, s -> s.createTextMessage(message));
        return logMessage + "\nSuccessful.";
    }

    @GetMapping("/topic/{message}")
    public String sendMessageToTopic(@PathVariable String message) {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Message to topic=[" + message + "].";
        log.info(logMessage);
        jmsTopicTemplate.send(jmsTopic, s -> s.createTextMessage(message));
        return logMessage + "\nSuccessful.";
    }
}
