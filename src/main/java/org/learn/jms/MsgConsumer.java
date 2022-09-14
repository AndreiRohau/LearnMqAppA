package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.learn.config.Msg;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MsgConsumer {

    @KafkaListener(topics = "${kafka.topic1.name}", groupId = "${kafka.topic1.consumer.group.id}")
    @SendTo
    public Msg listen(Msg msg) {
        log.info("LISTENING. MSG=[{}]", msg);
        msg.setProcessed(true);
        return msg;
    }

}
