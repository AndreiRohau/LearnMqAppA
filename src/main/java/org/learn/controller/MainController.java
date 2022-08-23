package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.Producer;
import org.learn.jms.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;

    private Publisher publisher;
    private Producer producer;

    @Autowired
    public MainController(Publisher publisher, Producer producer) {
        this.publisher = publisher;
        this.producer = producer;
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
        producer.sendMessage(message);
        return logMessage + "\nSuccessful.";
    }

    @GetMapping("/topic/{message}")
    public String sendMessageToTopic(@PathVariable String message) {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Message to topic=[" + message + "].";
        log.info(logMessage);
        publisher.createTopic(message);
        return logMessage + "\nSuccessful.";
    }
}
