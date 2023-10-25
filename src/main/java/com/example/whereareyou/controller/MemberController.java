package com.example.whereareyou.controller;

import com.example.whereareyou.dto.*;
import com.example.whereareyou.service.JwtTokenService;
import com.example.whereareyou.service.MemberService;
import com.example.whereareyou.vo.response.member.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ResponseLogin> login(@RequestBody MemberLoginRequest dto){
        ResponseLogin responseLogin = memberService.login(dto);

        return ResponseEntity.ok().body(responseLogin);
    }

    @PostMapping("/email/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request){
        memberService.authEmail(request.getEmail());

        return ResponseEntity.ok().body("코드가 전송 되었습니다.");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailRequest request){
        memberService.verifyEmailCode(request.getEmail(), request.getCode());

        return ResponseEntity.ok().body("코드가 일치 합니다");
    }

    @PostMapping("/email/verifyPassword")
    public ResponseEntity<ResponseResetPassword> verifyEmailResetPassword(@RequestBody PasswordReset reset){
        ResponseResetPassword resetPassword = memberService.verifyResetPasswordEmailCode(reset);

        return ResponseEntity.ok().body(resetPassword);
    }

    @PostMapping("/tokenReissue")
    public ResponseEntity<ResponseTokenReissue> reissue(@RequestBody TokenDto tokenDto){
        ResponseTokenReissue responseTokenReissue = jwtTokenService.reissueToken(tokenDto);

        return ResponseEntity.ok().body(responseTokenReissue);
    }

    @PostMapping("/findId")
    public ResponseEntity<ResponseFindId> findId(@RequestBody FindIdRequest request){
        ResponseFindId id = memberService.findId(request);

        return ResponseEntity.ok().body(id);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody CheckPasswordRequest request){
        memberService.passwordReset(request);

        return ResponseEntity.ok().body("비밀번호 재설정이 완료되었습니다.");
    }

    @DeleteMapping("/deleteMember")
    public ResponseEntity<Void> deleteMember(@RequestBody DeleteMemberRequest deleteMemberRequest){
        memberService.deleteMember(deleteMemberRequest);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myPage")
    public ResponseEntity<ResponseMember> getMyPage(@RequestParam("memberId") String memberId){
        ResponseMember memberPage = memberService.getMyPage(memberId);

        return ResponseEntity.ok().body(memberPage);
    }

    @PostMapping("/myPage/modify")
    public ResponseEntity<String> modifyMyPage(@RequestPart(value = "images",required = false) MultipartFile multipartFile,
                                         @RequestPart(value = "userId") String userId,
                                         @RequestPart(value = "newId",required = false) String newId) throws Exception {
        memberService.modifyMyPage(multipartFile,userId,newId);

        return ResponseEntity.ok().body("수정이 완료되었습니다.");
    }

}

