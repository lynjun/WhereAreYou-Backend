package com.example.whereareyou.service;

import com.example.whereareyou.domain.Member;
import com.example.whereareyou.dto.FindIdRequest;
import com.example.whereareyou.dto.MemberLoginRequest;
import com.example.whereareyou.dto.TokenDto;
import com.example.whereareyou.dto.resetPasswordDto;
import com.example.whereareyou.exception.customexception.*;
import com.example.whereareyou.repository.MemberRepository;
import com.example.whereareyou.vo.response.member.ResponseCheckEmail;
import com.example.whereareyou.vo.response.member.ResponseCheckId;
import com.example.whereareyou.vo.response.member.ResponseFindId;
import com.example.whereareyou.vo.response.member.ResponseResetPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final RedisTemplate<String, String > redisTemplate;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final JavaMailSender javaMailSender;
    private final JwtTokenService jwtTokenService;

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

    public TokenDto login(MemberLoginRequest memberLoginRequest) {
        Optional<Member> memberOptional = memberRepository.findByUserId(memberLoginRequest.getUserId());

        Member member = memberOptional.orElseThrow(() -> new UserNotFoundException("아이디 없음"));

        if (!encoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
            throw new PasswordMismatch("비밀번호 틀림");
        }

        return new TokenDto(
                jwtTokenService.generateAccessToken(member.getUserId()),
                jwtTokenService.generateRefreshToken(member.getUserId())
        );
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
        String text = "회원 가입을 위한 인증번호는 " + authKey + "입니다. <br/>";

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
        //유효 기간 5분동안 저장
        redisTemplate.opsForValue().set(email,
                authKey,
                300,
                TimeUnit.SECONDS);
    }

    public void verifyEmailCode(String email,String code){
        String verifyCode = redisTemplate.opsForValue().get(email);
        if(verifyCode == null){
            throw new TimeoutCode("시간이 초과되었습니다.");
        }
        if (!verifyCode.equals(code)){
            throw new InvalidCode("코드가 일치하지 않습니다.");
        }
    }

    public ResponseFindId findId(FindIdRequest request){
        Optional<Member> emailOptional = memberRepository.findByEmail(request.getEmail());

        Member member = emailOptional.orElseThrow(() -> new EmailNotFoundException("이메일 없음"));

        ResponseFindId responseFindId = new ResponseFindId();
        responseFindId.setUserId(member.getUserId());

        return responseFindId;

    }

}