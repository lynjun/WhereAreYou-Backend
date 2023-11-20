package com.example.whereareyou.global.controller;

import com.example.whereareyou.global.dto.FirebaseCloudMessageDTO;
import com.example.whereareyou.global.request.RequestFcmToken;
import com.example.whereareyou.global.service.FcmTokenService;
import com.example.whereareyou.global.service.FirebaseCloudMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/fcm")
public class FcmController {
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final FcmTokenService fcmTokenService;

    @Autowired
    public FcmController(FirebaseCloudMessageService firebaseCloudMessageService, FcmTokenService fcmTokenService) {
        this.firebaseCloudMessageService = firebaseCloudMessageService;
        this.fcmTokenService = fcmTokenService;
    }

    @PostMapping("/message")
    public ResponseEntity pushMessage(@RequestBody FirebaseCloudMessageDTO requestDTO) throws IOException {
        System.out.println(requestDTO.getTargetToken() + " "
                +requestDTO.getTitle() + " " + requestDTO.getBody());

        firebaseCloudMessageService.sendMessageTo(
                requestDTO.getTargetToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<Void> saveFcmToken(@RequestBody RequestFcmToken requestFcmToken){
        fcmTokenService.saveOrUpdateToken(requestFcmToken);

        return ResponseEntity.noContent().build();
    }
}
