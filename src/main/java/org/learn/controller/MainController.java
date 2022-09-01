package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;
    @Value("${rmq.declare.exchange}")
    private String exchangeName;
    @Value("${rmq.declare.routing.key.1}")
    private String routingKey;
    @Value("${rmq.declare.routing.key.2}")
    private String routingKeyAnother;

    private final AmqpTemplate template;

    @Autowired
    public MainController(AmqpTemplate template) {
        this.template = template;
    }

    @GetMapping("/test")
    public String test() {
        log.info("test");
        template.convertAndSend(exchangeName, routingKey, "Ninhao queue1");
        template.convertAndSend(exchangeName, routingKeyAnother, "Ninhao queue2");
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
