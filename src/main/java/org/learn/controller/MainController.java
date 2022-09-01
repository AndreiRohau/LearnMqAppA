package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;
    @Value("${routing.key.header}")
    private String routingKeyHeader;
    @Value("${spring.cloud.stream.rabbit.bindings.queue1Sink-in-0.consumer.bindingRoutingKey}")
    private String routingKey;
    @Value("${spring.cloud.stream.rabbit.bindings.queue2Sink-in-0.consumer.bindingRoutingKey}")
    private String routingKeyAnother;

    private final StreamBridge streamBridge;

    @Autowired
    public MainController(final StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @GetMapping("/test")
    public String test() {
        log.info("test");
        streamBridge.send("source-out-0",
                MessageBuilder
                        .withPayload("Nin hao queue1")
                        .setHeader(routingKeyHeader, routingKey)
                        .build());
        streamBridge.send("source-out-0",
                MessageBuilder
                        .withPayload("Nin hao queue2")
                        .setHeader(routingKeyHeader, routingKeyAnother)
                        .build());
        return "!!Success!!@@";
    }

    @GetMapping("/status")
    public String status() {
        log.info("CALLED: /status");
        return "[" + springApplicationName + "]. " + "Working!";
    }

    @GetMapping("/info")
    public String getInfo() {
        log.info("CALLED: /info");
        return "stubbed call /info result <br/>";
    }

    @GetMapping("/{destination}/")
    public String sendMessageToDestination(@PathVariable String destination, @RequestParam String message, @RequestParam boolean isPersistent) {
        log.info("[{}]." + "DEST=[{}]. MSG=[{}]. Persistence=[{}].", springApplicationName, destination, message, isPersistent);
        return "sender.getClass().getSimpleName()" + " sent=[" + message + "].";
    }

}
