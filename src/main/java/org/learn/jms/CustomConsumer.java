package org.learn.jms;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;

@Slf4j
public class CustomConsumer implements Consumer<Message<String>> {

    private final String consumerName;

    public CustomConsumer(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public void accept(Message<String> msg) {
        log.info("_____{}. STORES. MSG=[{}].", consumerName, msg.getPayload());
        writeToFile(msg.getPayload());

        log.info("_____{}. STORED. Successfully processed!!!", consumerName);
    }

    @SneakyThrows
    private void writeToFile(String messageText) {
        Path path = Paths.get("C:\\Users\\Andrei_Rohau\\IdeaProjects\\LearnMqAppA\\common.txt");
        Files.write(path, (messageText).getBytes());
    }

}
