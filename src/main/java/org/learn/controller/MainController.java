package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.learn.jms.Destination.TOPIC;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;
    private final Publisher publisher;

    @Autowired
    public MainController(Publisher publisher) {
        this.publisher = publisher;
    }

    @GetMapping("/status")
    public String status() {
        log.info("CALLED: /status");
        return "[" + springApplicationName + "]. " + "Working!";
    }

    @GetMapping("/info")
    public String getInfo() {
        log.info("CALLED: /info");
        return "TOPIC:EMITTED/PROCESSED=[" + TOPIC.getEmitted() + "/" + TOPIC.getProcessed() + "]<br/>" +
                TOPIC.getStringBuilder().toString();
    }

    @GetMapping("/TOPIC/")
    public String sendMessageToDestination(@RequestParam String message, @RequestParam boolean isPersistent) {
        log.info("[{}]: DESTINATION=[{}]. MSG=[{}]. PERSISTENCE=[{}].", springApplicationName, TOPIC, message, isPersistent);
        publisher.sendMessage(message, isPersistent);
        return "Publisher sent [" + message + "].";
    }
}
