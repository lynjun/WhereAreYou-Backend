package com.example.whereareyou.global.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.example.whereareyou.controller
 * fileName       : ElasticBeanstalkController
 * author         : pjh57
 * date           : 2023-10-05
 * description    : Load Balancer Health Check URl
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-05        pjh57       최초 생성
 */
@RestController
public class ElasticBeanstalkController {
    @GetMapping("/actuator/health")
    public ResponseEntity beanStalk() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
