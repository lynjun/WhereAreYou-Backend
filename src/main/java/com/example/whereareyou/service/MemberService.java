package com.example.whereareyou.service;

import com.example.whereareyou.domain.EmailCode;
import com.example.whereareyou.domain.Member;
import com.example.whereareyou.domain.RefreshToken;
import com.example.whereareyou.dto.*;
import com.example.whereareyou.exception.customexception.*;
import com.example.whereareyou.repository.EmailCodeRepository;
import com.example.whereareyou.repository.MemberRepository;
import com.example.whereareyou.repository.RefreshTokenRepository;
import com.example.whereareyou.vo.response.member.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
    public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final JavaMailSender javaMailSender;
    private final JwtTokenService jwtTokenService;
    private final EmailCodeRepository emailCodeRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AwsS3Service awsS3Service;

    public Member join(String userName, String userId, String password, String email){

        //저장
        Member member = Member.builder()
                .userName(userName)
                .userId(userId)
                .password(encoder.encode(password))
                .email(email)
                .profileImage(null)
                .build();
        return memberRepository.save(member);
    }

    public ResponseCheckId checkUserIdDuplicate(String userId){

        //userId 중복 체크
        memberRepository.findByUserId(userId).ifPresent(user -> {
            throw new UserIdDuplicatedException("이미 존재 하는 아이디");
        });

        ResponseCheckId responseCheckId = new ResponseCheckId();
        responseCheckId.setUserId(userId);
        responseCheckId.setMessage("사용 가능");

        return responseCheckId;

    }

    public ResponseCheckEmail checkEmailDuplicate(String email){

        //이메일 중복 체크
        memberRepository.findByEmail(email)
                .ifPresent(mail -> {
                    throw new EmailDuplicatedException("이미 존재 하는 이메일 입니다.");
                });

        ResponseCheckEmail responseCheckEmail = new ResponseCheckEmail();
        responseCheckEmail.setEmail(email);
        responseCheckEmail.setMessage("사용 가능한 이메일 입니다.");

        return responseCheckEmail;

    }

    public ResponseLogin login(MemberLoginRequest memberLoginRequest) {
        Optional<Member> memberOptional = memberRepository.findByUserId(memberLoginRequest.getUserId());

        Member member = memberOptional.orElseThrow(() -> new UserNotFoundException("아이디가 존재하지 않습니다."));

        if (!encoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
            throw new PasswordMismatch("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenService.generateAccessToken(member.getId());
        String refreshToken = jwtTokenService.generateRefreshToken(member.getId());

        ResponseLogin responseLogin = new ResponseLogin();

        responseLogin.setAccessToken(accessToken);
        responseLogin.setRefreshToken(refreshToken);
        responseLogin.setMemberId(member.getId());

        return responseLogin;
    }

    public void authEmail(String email){

        // authKey 생성
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) +  111111);

        //이메일 발송
        sendAuthEmail(email,authKey);
    }

    private void sendAuthEmail(String email,String authKey){

        String subject = "제목";
        String text = "인증번호는 " + authKey + "입니다. <br/>";

        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text,true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            throw new RuntimeException("이메일 형식이 유효 하지 않습니다.");
        }

        EmailCode emailCode = EmailCode.builder()
                .email(email)
                .code(authKey)
                .build();

        emailCodeRepository.save(emailCode);
    }

    public void verifyEmailCode(String email,String code){
        EmailCode byEmail = emailCodeRepository.findByEmail(email);
        String verifyCode = byEmail.getCode();

        if (!verifyCode.equals(code)){
            throw new InvalidCode("코드가 일치하지 않습니다.");
        }
        emailCodeRepository.delete(byEmail);
    }

    public ResponseResetPassword verifyResetPasswordEmailCode(PasswordReset reset){
        Optional<Member> emailOptional = memberRepository.findByEmail(reset.getEmail());

        Member member = emailOptional.orElseThrow(() -> new EmailNotFoundException("이메일 존재하지 않습니다."));

        if(!member.getUserId().equals(reset.getUserId())){
            throw new MemberMismatchException("회원 정보가 일치하지 않습니다.");
        }

        verifyEmailCode(reset.getEmail(),reset.getCode());

        ResponseResetPassword resetPassword = new ResponseResetPassword();
        resetPassword.setMessage("코드가 일치 합니다");
        resetPassword.setUserId(member.getUserId());

        return resetPassword;
    }

    public ResponseFindId findId(FindIdRequest request){

        verifyEmailCode(request.getEmail(),request.getCode());

        Optional<Member> emailOptional = memberRepository.findByEmail(request.getEmail());
        Member member = emailOptional.orElseThrow(() -> new EmailNotFoundException("이메일 없음"));

        ResponseFindId responseFindId = new ResponseFindId();
        responseFindId.setUserId(member.getUserId());

        return responseFindId;

    }

    public void passwordReset(CheckPasswordRequest request) {

        Optional<Member> byUserId = memberRepository.findByUserId(request.getUserId());

        Member member = byUserId.orElseThrow(() -> new EmailNotFoundException("아이디 없음"));

        if (!request.getPassword().equals(request.getCheckPassword())){
            throw new ResetPasswordMismatch("비밀번호가 일치 하지 않습니다.");
        }

        member.setPassword(encoder.encode(request.getPassword()));

        memberRepository.save(member);

    }

    public void deleteMember(DeleteMemberRequest deleteMemberRequest){

        Optional<Member> byId = memberRepository.findById(deleteMemberRequest.getMemberId());
        byId.orElseThrow(() -> new UserNotFoundException("아이디가 없습니다"));

        RefreshToken byMemberId = refreshTokenRepository.findByMemberId(deleteMemberRequest.getMemberId());

        refreshTokenRepository.delete(byMemberId);

        memberRepository.deleteById(deleteMemberRequest.getMemberId());

    }
    public ResponseMember getMyPage(String memberId){
        Optional<Member> byId = memberRepository.findById(memberId);
        Member member = byId.orElseThrow(() -> new UserNotFoundException("아이디가 없습니다"));

        String userName = member.getUserName();
        String userId = member.getUserId();
        String email = member.getEmail();

        ResponseMember responseMember = new ResponseMember();
        responseMember.setUserName(userName);
        responseMember.setUserId(userId);
        responseMember.setEmail(email);

        return responseMember;
    }

    public void modifyMyPage(MultipartFile multipartFile,String userId,String newId) throws Exception {
        //멤버 찾기
        Optional<Member> byUserId = memberRepository.findByUserId(userId);
        Member member = byUserId.orElseThrow(() -> new UserNotFoundException("아이디가 없습니다"));

        //아이디 변경
        if(newId!=null){
            //아이디 중복 체크
            Optional<Member> byNewId = memberRepository.findByUserId(newId);
            byNewId.orElseThrow(() -> new UserIdDuplicatedException("중복된 아이디 입니다."));

            member.setUserId(newId);
        }
        //프로필 사진
        if(multipartFile!=null){
            String upload = awsS3Service.upload(multipartFile);
            member.setProfileImage(upload);
        }
        //저장
        memberRepository.save(member);

    }
}