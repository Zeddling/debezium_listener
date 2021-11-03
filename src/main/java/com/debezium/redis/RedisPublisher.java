package com.debezium.redis;

import com.debezium.model.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * Handles all publish to the redis topic
 * */
@Service
public class RedisPublisher implements MessagePublisher {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic topic;

    public RedisPublisher() {
    }

    public RedisPublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(final StudentDTO payload) {
        redisTemplate.convertAndSend(topic.getTopic(), payload);
    }
}
