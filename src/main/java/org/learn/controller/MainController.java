package org.learn.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class MainController {
    private static final String MESSAGE_EXCHANGE = "source-out-0";
    private static final String routingKeyHeader = "myRoutingKey";
    private static final String routingKey = "routing-queue1";
    @Value("${spring.application.name}")
    private String springApplicationName;

    private final StreamBridge streamBridge;

    @Autowired
    public MainController(final StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @GetMapping("/test")
    public String test() {
        log.info("test");
        List<String> letters = List.of("a", "b", "c7", "d7", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n");
        for (String letter : letters) {
            streamBridge.send(MESSAGE_EXCHANGE,
                    MessageBuilder
                            .withPayload("Nin hao queue " + letter)
                            .setHeader(routingKeyHeader, routingKey)
                            .build());
        }
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
        return getFailedMessage();
    }

    @SneakyThrows
    private String getFailedMessage() {
        Path path = Paths.get("C:\\Users\\Andrei_Rohau\\IdeaProjects\\LearnMqAppA\\common.txt");
        List<String> strings = Files.readAllLines(path);
        String s = strings.isEmpty() ? "" : strings.get(0);
        Files.write(path, "".getBytes(StandardCharsets.UTF_8));
        return s;
    }

    @GetMapping("/source1-out-0/") // initQ
    public String sendMessageToDestination(@RequestParam String message) {
        log.info("CALLED: #sendMessageToDestination destination={}. message={}", MESSAGE_EXCHANGE, message);
        // destination = "source1-out-0"
        streamBridge.send(MESSAGE_EXCHANGE,
                MessageBuilder
                        .withPayload(message)
                        .setHeader(routingKeyHeader, routingKey)
                        .build());
        return "streamBridge.send([" + MESSAGE_EXCHANGE + "], MessageBuilder.withPayload([" +
                message + "]).setHeader(routingKeyHeader, routingKey).build())";
    }

}
