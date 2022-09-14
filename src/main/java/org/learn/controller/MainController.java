package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {
    private static final String MESSAGE_EXCHANGE = "source1-out-0";
    @Value("${spring.application.name}")
    private String springApplicationName;

    @GetMapping("/test")
    public String test() {
        log.info("test");
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
        return "INFO STUBBED";
    }

    @GetMapping("/source1-out-0/") // initQ
    public String sendMessageToDestination(@RequestParam String message) {
        log.info("CALLED: #sendMessageToDestination destination={}. message={}", "*STUB*", message);
        // destination = "source1-out-0"
        return "message-sent";
    }
}
