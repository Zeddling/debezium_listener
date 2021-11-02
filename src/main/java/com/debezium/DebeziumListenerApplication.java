package com.debezium;

import com.debezium.model.Student;
import com.debezium.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DebeziumListenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebeziumListenerApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(StudentRepository repository) {
		return  (args)-> {
			repository.save(
					new Student(
							"John Doe", "john@email.com", "07012345678", true
					)
			);
			repository.save(
					new Student(
							"Jane Doe", "jane@email.com", "0787654321", false
					)
			);
		};
	}

}
