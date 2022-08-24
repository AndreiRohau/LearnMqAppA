package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;
    private Publisher publisher;

    @Autowired
    public MainController(Publisher publisher) {
        this.publisher = publisher;
    }

    @GetMapping("/status")
    public String status() {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Working!";
        log.info(logMessage);
        return logMessage;
    }

    @GetMapping("/info")
    public String getInfo() {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Getting info!";
        log.info(logMessage);
        return "";
    }

    @GetMapping("/{destination}/")
    public String sendMessageToDestination(@PathVariable String destination, @RequestParam String message, @RequestParam boolean isPersistent) throws JMSException {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Message to "
                + destination + "=[" + message + "]. Persistance=[" + isPersistent + "].";
        log.info(logMessage);
        publisher.sendMessage(message, isPersistent);
        return logMessage + "\nSuccessful.";
    }
}
