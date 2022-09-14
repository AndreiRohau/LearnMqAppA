package org.learn.jms;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.APPEND;

public class Util {
    @SneakyThrows
    public static void safeSleep(int secs) {
        TimeUnit.SECONDS.sleep(secs);
    }

    @SneakyThrows
    private String getFailedMessage() {
        Path path = Paths.get("C:\\Users\\Andrei_Rohau\\IdeaProjects\\LearnMqAppA\\common.txt");
        List<String> strings = Files.readAllLines(path);
        String s = strings.isEmpty() ? "" : strings.get(0);
        Files.write(path, "".getBytes(StandardCharsets.UTF_8));
        return s;
    }

    @SneakyThrows
    public static void writeToFile(String messageText) {
        Path path = Paths.get("C:\\Users\\Andrei_Rohau\\IdeaProjects\\LearnMqAppA\\tracker.txt");
        Files.write(path, (LocalDateTime.now() + messageText + "\n").getBytes(), APPEND);
    }
}
