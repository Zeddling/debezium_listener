package com.debezium.service;

import com.debezium.model.Student;
import com.debezium.model.StudentDTO;
import com.debezium.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles Server sent events.
 * */
@Service
@Slf4j
public class EmitterService {
    List<SseEmitter> emitters = new ArrayList<>();

    @Autowired
    StudentRepository repository;

    /**
     * Adds an emitter to the emitter store
     *
     * @param emitter new emitter
     * */
    public void addEmitter(SseEmitter emitter) {
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitters.add(emitter);
    }

    /**
     * Pushes updates to all emitters
     * Removes any dead emitters
     *
     * @param payload new enrolled student
     * */
    public void pushNotification(StudentDTO payload) {
        log.info("pushing notification for user {}", payload.getName());
        List<SseEmitter> deadEmitters = new ArrayList<>();

        //  Noticed that phone number field was empty
        //  will investigate upon motivation
        if (payload.getPhoneNumber() == "") {
            Optional<Student> studentOptional = repository.findById(payload.getId());
            if (studentOptional.isPresent()) {
                payload.setPhoneNumber(
                        studentOptional.get().getPhoneNumber()
                );
            }
        }
        
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter
                        .event()
                        .data(payload));

            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });

        emitters.removeAll(deadEmitters);
        log.info("Done sending notifications");
    }
}
