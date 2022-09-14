package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.config.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
public class MainController {
    private static final String MESSAGE_EXCHANGE = "source1-out-0";
    @Value("${spring.application.name}")
    private String springApplicationName;
    @Value("${kafka.topic1.name}")
    private String topic1Name;

    @Autowired
    private KafkaTemplate<String, Msg> kafkaTemplate;

    @GetMapping("/test")
    public String test() {
        log.info("test");
        final String ruid = UUID.randomUUID().toString();
        final Msg msg = new Msg(ruid, "head", "body");
        log.info("SENDING. MSG=[{}]", msg);
        kafkaTemplate.send(topic1Name, ruid, msg);
        return "!!Success!!@@ sent msg=" + msg;
    }

    @GetMapping("/status")
    public String status() {
        log.info("CALLED: /status");
        return "[" + springApplicationName + "]. " + "Working!";
    }

    @GetMapping("/info")
    public String getInfo() {
        log.info("CALLED: /info");
        return "INFO STUBBED";
    }

    @GetMapping("/call/") // initQ
    public String sendMessageToDestination(@RequestParam String message) {
        log.info("CALLED: #sendMessageToDestination destination={}. message={}", "*STUB*", message);
        // destination = "source1-out-0"
        return "message-sent";
    }
}
