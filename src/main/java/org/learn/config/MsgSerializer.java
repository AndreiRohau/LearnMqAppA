package org.learn.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class MsgSerializer implements Serializer<Msg> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, Msg msg) {
        try {
            log.info("SERIALIZING. S=[{}]. msg=[{}]", s, msg);
            return objectMapper.writeValueAsBytes(msg);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Could not serialize MSG", e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Msg data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
