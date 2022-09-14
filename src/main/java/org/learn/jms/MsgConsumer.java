package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.learn.config.Msg;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MsgConsumer {

    @KafkaListener(topics = "${kafka.topic1.name}", groupId = "${kafka.topic1.consumer.group.id}")
    public void listen(Msg msg) {
        log.info("LISTENING. MSG=[{}]", msg);
    }
}
