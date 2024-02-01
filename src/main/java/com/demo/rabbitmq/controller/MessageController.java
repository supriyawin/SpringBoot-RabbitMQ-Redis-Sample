package com.demo.rabbitmq.controller;

import com.demo.rabbitmq.dto.Student;
import com.demo.rabbitmq.publisher.RabbitMQProducer;
import com.demo.rabbitmq.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private final RabbitMQProducer rabbitMQProducer;

    @Autowired
    private StudentRepository studentRepository;




    public MessageController(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @GetMapping("/checkConnectivity/{id}")
    public ResponseEntity<String> sendMessage(@PathVariable String id)
    {
        try {
            rabbitMQProducer.sendMessage("Testing demo message");
        }catch (Exception e)
        {
            return new ResponseEntity<>(String.format("Failed to publish Message : %s ",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
        Student student = new Student(id, "John Doe",  1);
        studentRepository.saveStudent(student);

        }catch (Exception e)
        {
            return new ResponseEntity<>(String.format("Failed to save data to Reddis : %s ",e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("Connection to RabbitMQ and Reddis Successful ...");
    }

    @GetMapping("/getStudent/{id}")
    public Student getStudent(@PathVariable String id)
    {
        Student retrievedStudent =
                studentRepository.getStudent(id);
        return retrievedStudent;
    }
}
