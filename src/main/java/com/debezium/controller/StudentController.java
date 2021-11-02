package com.debezium.controller;

import com.debezium.model.StudentDTO;
import com.debezium.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;


    //  List all students on page
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute(
                "students",
                studentRepository.findByEnrolledTrue()
        );
        return "index";
    }

}
