package com.debezium.redis;

import com.debezium.model.StudentDTO;
import com.debezium.service.EmitterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

/**
 * Listens for uploads to the topic and sends a payload to the
 * emmiter service
 * */
@Service
@Slf4j
public class RedisSubscriber implements MessageListener {

    @Autowired
    private EmitterService emitterService;

    ObjectMapper objectMapper = new ObjectMapper();

    public void onMessage(final Message message, final byte[] pattern) {
        try {
            var payload = objectMapper.readValue(message.toString(), StudentDTO.class);

            log.info("Received {}", payload);
            emitterService.pushNotification(payload);

        } catch (JsonProcessingException e) {
            log.error("unable to deserialize message ", e);
        }
    }
}
