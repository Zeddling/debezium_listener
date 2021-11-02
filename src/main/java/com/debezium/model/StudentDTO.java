package com.debezium.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 *  Transfer object for the Student POJO over the websocket.
 *
 *  Expects u or d letters representing update and delete operations respectively
 * */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Setter
@ToString
public class StudentDTO {
    private String operation;
    private Student student;
}
