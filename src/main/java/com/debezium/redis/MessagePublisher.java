package com.debezium.redis;

import com.debezium.model.StudentDTO;

public interface MessagePublisher {
    void publish(final StudentDTO payload);
}
