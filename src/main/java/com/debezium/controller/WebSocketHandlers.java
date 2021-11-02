package com.debezium.controller;

import com.debezium.model.StudentDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

public class WebSocketHandlers {

    @MessageMapping("/students")
    @SendTo("/topic/students")
    public StudentDTO broadcastNewEnrolledStudent(StudentDTO message) {
        return message;
    }

}
