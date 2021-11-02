package com.debezium.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String phoneNumber;
    private boolean enrolled;

    public Student(String name, String email, String phoneNumber, boolean enrolled) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.enrolled = enrolled;
    }
}
