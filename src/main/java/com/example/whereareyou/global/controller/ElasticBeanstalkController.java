package com.example.whereareyou.global.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElasticBeanstalkController {
    @GetMapping("/actuator/health")
    public ResponseEntity beanStalk() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
