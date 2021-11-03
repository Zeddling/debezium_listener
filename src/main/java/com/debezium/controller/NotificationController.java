package com.debezium.controller;

import com.debezium.service.EmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private EmitterService service;

    @GetMapping("/subscribe")
    public SseEmitter subsribe() {
        log.info("subscribing...");

        SseEmitter sseEmitter = new SseEmitter(24 * 60 * 60 * 1000l);
        service.addEmitter(sseEmitter);

        log.info("subscribed");
        return sseEmitter;
    }



}
