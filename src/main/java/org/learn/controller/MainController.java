package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;

import static org.learn.jms.Destination.QUEUE;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;
    private final Producer producer;

    @Autowired
    public MainController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/status")
    public String status() {
        log.info("CALLED: /status");
        return "[" + springApplicationName + "]. " + "Working!";
    }

    @GetMapping("/info")
    public String getInfo() {
        log.info("CALLED: /info");
        return "QUEUE:EMITTED/PROCESSED=[" + QUEUE.getEmitted() + "/" + QUEUE.getProcessed() + "]<br/>" +
                QUEUE.getStringBuilder().toString();
    }

    @GetMapping("/QUEUE/")
    public String sendMessageToDestination(@RequestParam String message, @RequestParam boolean isPersistent) throws JMSException {
        log.info("[{}]." + "DEST=[{}]. MSG=[{}]. Persistence=[{}].", springApplicationName, QUEUE, message, isPersistent);
        producer.sendMessage(message, isPersistent);
        return "Producer sent=[" + message + "].";
    }
}
