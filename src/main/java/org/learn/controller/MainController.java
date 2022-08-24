package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.example.RequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;

    private RequestProcessor requestProcessor;

    @Autowired
    public MainController(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;

    }

    @GetMapping("/status")
    public String status() {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Working!";
        log.info(logMessage);
        return logMessage;
    }

    @GetMapping("/{message}/")
    public String sendMessage(@PathVariable String message) throws JMSException {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Message=[" + message + "].";
        log.info(logMessage);
        requestProcessor.processRequest(message);
        return logMessage + "\nSuccessful.";
    }
}
