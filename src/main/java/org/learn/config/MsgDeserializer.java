package org.learn.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class MsgDeserializer implements Deserializer<Msg> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public Msg deserialize(String s, byte[] bytes) {
        try {
            log.info("DESERIALIZING. S=[{}]. byte[]=[{}]", s, bytes);
            return objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), Msg.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not deserialize MSG-BYTES", e);
        }
    }

    @Override
    public Msg deserialize(String topic, Headers headers, byte[] data) {
        return Deserializer.super.deserialize(topic, headers, data);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
