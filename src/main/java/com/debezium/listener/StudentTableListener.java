package com.debezium.listener;

import com.debezium.model.Student;
import com.debezium.model.StudentDTO;
import com.debezium.redis.RedisPublisher;
import com.debezium.utils.Operation;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static io.debezium.data.Envelope.FieldName.*;
import static java.util.stream.Collectors.toMap;
/**
 * Handles creation, start and stoppage of the debezium engine.
 * */

@Component
@Slf4j
public class StudentTableListener {

    /**
     * Thread pool for running Debezium asynchronously
     * */
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Debezium engine
     * */
    private final EmbeddedEngine engine;

    @Autowired
    RedisPublisher publisher;

    public StudentTableListener(Configuration connector) {
        this.engine = EmbeddedEngine
                .create()
                .using(connector)
                .notifying(this::handleEvent).build();
    }

    /**
     * Initialize engine asynchronously with the executor
     *
     * @see PostConstruct
     * */
    @PostConstruct
    private void start() {
        this.executor.execute(engine);
    }

    /**
     * Merges executor thread to main thread
     *
     * @see PreDestroy
     * */
    @PreDestroy
    private void stop() {
        if (this.engine != null) {
            this.engine.stop();
        }
    }


    /**
     * Handle database events. Monitor the enrolled field.
     * */
    private void handleEvent(SourceRecord sourceRecord) {
        Struct value = (Struct) sourceRecord.value();

        //  Assert value is not null
        if (value != null) {
            Operation operation = Operation.forCode((String) value.get(OPERATION));

            //  Assert it's a transactional operation
            if (operation != Operation.READ) {
                Map<String, Object> objects;
                String record = AFTER;
                StudentDTO payload = new StudentDTO();

                if (operation == Operation.DELETE) {
                    record = BEFORE;
                    payload.setOperation("d");
                } else {
                    payload.setOperation("u");
                }

                //  Build map
                Struct struct = (Struct) value.get(record);
                objects = struct.schema().fields().stream()
                        .map(Field::name)
                        .filter(name -> struct.get(name) != null)
                        .map(name -> Pair.of(name, struct.get(name)))
                        .collect(toMap(Pair::getKey, Pair::getValue));

                Student student = toStudent(objects);

                //  Send update only when student is enrolled
                if (student.isEnrolled()) {
                    payload.setId(student.getId());
                    payload.setName(student.getName());
                    payload.setEmail(student.getEmail());
                    payload.setPhoneNumber(student.getPhoneNumber());

                    log.info(payload.toString());
                    log.info("Student added: {}", student.getName());
                    publisher.publish(payload);
                }
            }
        }
    }

    /**
     * Derive student from the provided map
     *
     * @param map Derived map from struct
     * @see ObjectMapper
     * @return Student POJO
     * */
    private Student toStudent(Map<String, Object> map) {
        //  Get class POJO
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(map, Student.class);
    }

}






