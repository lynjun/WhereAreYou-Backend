package com.example.whereareyou.controller;

import com.example.whereareyou.dto.*;
import com.example.whereareyou.service.JwtTokenService;
import com.example.whereareyou.service.MemberService;
import com.example.whereareyou.vo.response.member.ResponseCheckEmail;
import com.example.whereareyou.vo.response.member.ResponseCheckId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;


    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinRequest dto){

        memberService.join(dto.getUserName(), dto.getUserId(), dto.getPassword(), dto.getEmail());

        return ResponseEntity.ok().body("회원가입 성공");
    }


    @GetMapping("/checkId")
    public ResponseEntity<ResponseCheckId> checkUserIdDuplicate1(@RequestParam("userId") CheckUserIdDuplicateRequest dto){
        ResponseCheckId responseCheckId = memberService.checkUserIdDuplicate(dto.getUserId());

        return ResponseEntity.ok().body(responseCheckId);
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<ResponseCheckEmail> checkEmailDuplicate(@RequestParam("email") CheckEmailRequest dto){
        ResponseCheckEmail responseCheckEmail = memberService.checkEmailDuplicate(dto.getEmail());

        return ResponseEntity.ok().body(responseCheckEmail);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginRequest dto){
        TokenDto tokenDto = memberService.login(dto);
        return ResponseEntity.ok().body(tokenDto);
    }

    @PostMapping("/email")
    public ResponseEntity<String> authEmail(@RequestBody EmailRequest request){
        memberService.authEmail(request.getEmail());
        return ResponseEntity.ok().body("코드가 전송 되었습니다.");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailRequest request){
        memberService.verifyEmailCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok().body("코드가 일치 합니다");
    }

    @PostMapping("/tokenReissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenDto tokenDto){

        tokenDto = jwtTokenService.reissueToken(tokenDto);

        return ResponseEntity.ok().body(tokenDto);

    }

}

