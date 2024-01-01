package com.example.whereareyou.member.controller;

import com.example.whereareyou.member.dto.CheckEmailRequest;
import com.example.whereareyou.member.dto.*;
import com.example.whereareyou.member.response.*;
import com.example.whereareyou.global.service.JwtTokenService;
import com.example.whereareyou.member.service.MemberService;
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
    public ResponseEntity<Void> join(@RequestBody MemberJoinRequest dto){
        memberService.join(dto.getUserName(), dto.getUserId(), dto.getPassword(), dto.getEmail());

        return ResponseEntity.noContent().build();
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
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest email){
        memberService.authEmail(email.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyEmail(@RequestBody EmailRequest request){
        memberService.verifyEmailCode(request.getEmail(), request.getCode());

        return ResponseEntity.noContent().build();
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
    public ResponseEntity<Void> resetPassword(@RequestBody CheckPasswordRequest request){
        memberService.passwordReset(request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteMember")
    public ResponseEntity<Void> deleteMember(@RequestBody DeleteMemberRequest deleteMemberRequest){
        memberService.deleteMember(deleteMemberRequest);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/details")
    public ResponseEntity<ResponseMember> getDetailMember(@RequestParam("memberId") String memberId){
        ResponseMember memberPage = memberService.getDetailMember(memberId);

        return ResponseEntity.ok().body(memberPage);
    }

    @PostMapping("/myPage/modify")
    public ResponseEntity<Void> modifyMyPage(@RequestPart(value = "memberId") String memberId,
                                         @RequestPart(value = "images",required = false) MultipartFile multipartFile,
                                         @RequestPart(value = "newId",required = false) String newId,
                                         @RequestPart(value = "newUserName",required = false) String userName) throws Exception {
        memberService.modifyMyPage(multipartFile,memberId,newId,userName);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseMemberByUserId> getDetailMemberByUserId(@RequestParam String userId){

        ResponseMemberByUserId detailMemberByUserId = memberService.getDetailMemberByUserId(userId);

        return ResponseEntity.ok().body(detailMemberByUserId);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String memberId){

        memberService.logout(memberId);

        return ResponseEntity.ok().build();
    }

}

