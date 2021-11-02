package com.debezium.repository;

import com.debezium.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByEnrolledTrue();
}
