package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.Destination;
import org.learn.jms.Producer;
import org.learn.jms.Publisher;
import org.learn.jms.Sendable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.learn.jms.Destination.QUEUE;
import static org.learn.jms.Destination.TOPIC;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;

    private Publisher publisher;
    private Producer producer;

    private Map<Destination, Sendable> mqMap = new HashMap<>();

    @Autowired
    public MainController(Publisher publisher, Producer producer) {
        this.publisher = publisher;
        this.producer = producer;
        mqMap.put(QUEUE, producer);
        mqMap.put(TOPIC, publisher);
    }

    @GetMapping("/status")
    public String status() {
        log.info("CALLED: /status");
        return "[" + springApplicationName + "]. " + "Working!";
    }

    @GetMapping("/info")
    public String getInfo() {
        log.info("CALLED: /info");
        return "QUEUE:EMITTED/PROCESSED=[" + QUEUE.getEmitted() + "/" + QUEUE.getProcessed() + "] <br/> " +
                "TOPIC:EMITTED/PROCESSED=[" + TOPIC.getEmitted() + "/" + TOPIC.getProcessed() + "] <br/> " +
                QUEUE.getStringBuilder().toString();
    }

    @GetMapping("/{destination}/")
    public String sendMessageToDestination(@PathVariable Destination destination, @RequestParam String message, @RequestParam boolean isPersistent) {
        log.info("[{}]." + "DEST=[{}]. MSG=[{}]. Persistence=[{}].", springApplicationName, destination, message, isPersistent);
        Sendable sender = mqMap.get(destination);
        sender.sendMessage(message, isPersistent);
        return sender.getClass().getSimpleName()+ " sent=[" + message + "].";
    }
}
