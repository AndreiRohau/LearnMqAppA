package org.learn.controller;

import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.learn.jms.Publisher;
import org.learn.jms.RabbitEnvelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;
    @Value("${rmq.declare.exchange}")
    private String exchangeName;
    @Value("${rmq.declare.routing.key}")
    private String routingKey;
    @Value("${rmq.declare.queue}")
    private String queueName;

    private final Publisher publisher;

    @Autowired
    public MainController(Publisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    private void postConstruct() {

    }

    @GetMapping("/test")
    public String test() {
        log.info("test");
        RabbitEnvelope envelope = RabbitEnvelope.builder()
                .message("hi hi hi test msg")
                .exchange(exchangeName)
                .queue(exchangeName)
                .routingKey(routingKey)
                .isMandatory(true)
                .properties(MessageProperties.PERSISTENT_TEXT_PLAIN)
                .build();
        publisher.publishEvent(envelope);
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
        send(destination, message, isPersistent);
        return "sender.getClass().getSimpleName()" + " sent=[" + message + "].";
    }

    private void send(String destination, String message, boolean isPersistent) {
        log.info("send method worked");
//        Object template = jmsTemplateMap.get(destination);
//        template.setDeliveryPersistent(isPersistent);
//        template.convertAndSend(destinationNameMap.get(destination), message);
    }
}
