package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.learn.jms.Destination.QUEUE;
import static org.learn.jms.Destination.TOPIC;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;
    @Value("${activemq.queue}")
    private String queueName;
    @Value("${activemq.topic}")
    private String topicName;

    private final JmsTemplate jmsQueueTemplate;
    private final JmsTemplate jmsTopicTemplate;
    private final Map<Destination, JmsTemplate> jmsTemplateMap = new HashMap<>();
    private final Map<Destination, String> destinationNameMap = new HashMap<>();

    @Autowired
    public MainController(JmsTemplate jmsQueueTemplate, JmsTemplate jmsTopicTemplate) {
        this.jmsQueueTemplate = jmsQueueTemplate;
        this.jmsTopicTemplate = jmsTopicTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        jmsTemplateMap.put(QUEUE, jmsQueueTemplate);
        jmsTemplateMap.put(TOPIC, jmsTopicTemplate);
        destinationNameMap.put(QUEUE, queueName);
        destinationNameMap.put(TOPIC, topicName);
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
        send(destination, message, isPersistent);
        return "sender.getClass().getSimpleName()" + " sent=[" + message + "].";
    }

    private void send(Destination destination, String message, boolean isPersistent) {
        JmsTemplate template = jmsTemplateMap.get(destination);
        template.setDeliveryPersistent(isPersistent);
        template.convertAndSend(destinationNameMap.get(destination), message);
    }
}
