package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.learn.config.Msg;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.learn.jms.Util.safeSleep;
import static org.learn.jms.Util.writeToFile;

@Slf4j
@Component
public class MsgConsumer3 {

    @KafkaListener(topics = "${kafka.topic1.name}", groupId = "${kafka.topic1.consumer.group.id}")
    @SendTo
    public Msg listen(Msg msg) {
        log.info("LISTENING. MSG=[{}]", msg);
        msg.setProcessed(true);
        safeSleep(2);
        writeToFile(msg.toString());
        safeSleep(2);
        return msg;
    }

}
