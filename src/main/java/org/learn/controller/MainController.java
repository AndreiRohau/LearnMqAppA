package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.learn.config.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class MainController {
    private static final String MESSAGE_EXCHANGE = "source1-out-0";
    @Value("${spring.application.name}")
    private String springApplicationName;
    @Value("${kafka.topic1.name}")
    private String topic1Name;

//    @Autowired
//    private KafkaTemplate<String, Msg> kafkaTemplate;
    @Autowired
    private ReplyingKafkaTemplate<String, Msg, Msg> replyingKafkaTemplate;
//    @Autowired
//    private Runnable produce;
//    @Autowired
//    private Runnable produceTx;

    @GetMapping("/v1/test")
    @Transactional
    public String test() {
        log.info("test");
        final String ruid = UUID.randomUUID().toString();
        final Msg msg = new Msg(ruid, "head", "body", false);
        log.info("SENDING. MSG=[{}]", msg);
        replyingKafkaTemplate.send(topic1Name, ruid, msg);
        return "!!Success!!@@ sent msg=" + msg;
    }

//    @GetMapping("/v2/test")
//    @Transactional
//    public String testV2() {
//        log.info("test");
//        final Msg msg = new Msg(UUID.randomUUID().toString(), "head", "body", false);
//        final ProducerRecord<String, Msg> record = new ProducerRecord<>(topic1Name, msg.getUid(), msg);
//        log.info("SENDING. record=[{}]", record);
//        try {
//            ConsumerRecord<String, Msg> response = replyingKafkaTemplate.sendAndReceive(record, Duration.ofMillis(10000)).get();
//            return "!!Success!!@@ sent msg=" + response.value();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @GetMapping("/v3/test")
//    @Transactional
//    public String testV3() {
//        log.info("test");
//        produce.run();
//        return "!!Success!!@@ sent msg=v3";
//    }
//
//    @GetMapping("/v4/test")
//    @Transactional
//    public String testV4() {
//        log.info("test");
//        produceTx.run();
//        return "!!Success!!@@ sent msg=v4";
//    }

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
