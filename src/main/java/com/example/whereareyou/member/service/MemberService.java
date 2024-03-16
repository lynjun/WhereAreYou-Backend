package com.example.whereareyou.member.service;

import com.example.whereareyou.emailCode.domain.EmailCode;
import com.example.whereareyou.emailCode.repository.EmailCodeRepository;
import com.example.whereareyou.global.repository.FcmTokenRepository;
import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.member.dto.*;
import com.example.whereareyou.member.exception.*;
import com.example.whereareyou.memberInfo.repository.MemberInfoRepository;
import com.example.whereareyou.member.repository.MemberRepository;
import com.example.whereareyou.member.response.*;
import com.example.whereareyou.memberSchedule.repository.MemberScheduleRepository;
import com.example.whereareyou.refreshToken.repository.RefreshTokenRepository;
import com.example.whereareyou.global.service.AwsS3Service;
import com.example.whereareyou.global.service.JwtTokenService;
import com.example.whereareyou.schedule.domain.Schedule;
import com.example.whereareyou.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
    private final MemberInfoRepository memberInfoRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberScheduleRepository memberScheduleRepository;

    public void join(String userName, String userId, String password, String email){
        //저장
        Member member = saveMember(userName, userId, password, email);

        memberRepository.save(member);
    }

    private Member saveMember(String userName, String userId, String password, String email) {
        return Member.builder()
                .userName(userName)
                .userId(userId)
                .password(encoder.encode(password))
                .email(email)
                .profileImage(null)
                .build();
    }

    public ResponseCheckId checkUserIdDuplicate(String userId){
        //userId 중복 체크
        duplicateUserId(userId);

        return setResponseCheckId(userId);

    }

    private static ResponseCheckId setResponseCheckId(String userId) {
        ResponseCheckId responseCheckId = new ResponseCheckId();
        responseCheckId.setUserId(userId);
        return responseCheckId;
    }

    private void duplicateUserId(String userId) {
        memberRepository.findByUserId(userId)
                .ifPresent(user -> {
            throw new UserIdDuplicatedException("이미 존재 하는 아이디");
        });
    }

    public ResponseCheckEmail checkEmailDuplicate(String email){
        //이메일 중복 체크
        duplicateEmail(email);

        return setResponseCheckEmail(email);

    }

    private static ResponseCheckEmail setResponseCheckEmail(String email) {
        ResponseCheckEmail responseCheckEmail = new ResponseCheckEmail();
        responseCheckEmail.setEmail(email);
        return responseCheckEmail;
    }

    private void duplicateEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(mail -> {
                    throw new EmailDuplicatedException("이미 존재 하는 이메일 입니다.");
                });
    }

    public ResponseLogin login(MemberLoginRequest memberLoginRequest) {
        Member member = returnMember(memberLoginRequest.getUserId());

        checkPassword(memberLoginRequest, member);

        String accessToken = jwtTokenService.generateAccessToken(member.getId());
        String refreshToken = jwtTokenService.generateRefreshToken(member.getId());

        return setResponseLogin(accessToken, refreshToken, member);
    }

    private static ResponseLogin setResponseLogin(String accessToken, String refreshToken, Member member) {
        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setAccessToken(accessToken);
        responseLogin.setRefreshToken(refreshToken);
        responseLogin.setMemberId(member.getId());
        return responseLogin;
    }

    private void checkPassword(MemberLoginRequest memberLoginRequest, Member member) {
        if (!encoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
            throw new PasswordMismatch("비밀번호가 일치하지 않습니다.");
        }
    }

    private Member returnMember(String userId) {
        Optional<Member> memberOptional = memberRepository.findByUserId(userId);
        return memberOptional.orElseThrow(() ->
                new UserNotFoundException("아이디가 존재하지 않습니다."));
    }

    public void authEmail(String email){
        // authKey 생성
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) +  111111);

        //이메일 발송
        sendAuthEmail(email,authKey);
    }

    private void sendAuthEmail(String email, String authKey){
        EmailCode byEmail = emailCodeRepository.findByEmail(email);

        String subject = "지금어디 인증번호 입니다.";
        String text = "인증번호는 " + authKey + "입니다. <br/>";

        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text,true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            throw new InvalidEmailException("이메일 형식이 유효 하지 않습니다.");
        }

        saveCode(email, authKey, byEmail);
    }

    private void saveCode(String email, String authKey, EmailCode byEmail) {
        if(byEmail != null){
            byEmail.setCode(authKey);
            emailCodeRepository.save(byEmail);
        }

        if(byEmail == null ) {
            EmailCode emailCode = EmailCode.builder()
                    .email(email)
                    .code(authKey)
                    .build();
            emailCodeRepository.save(emailCode);
        }
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
        Member member = returnMemberByEmail(reset.getEmail());

        checkUserId(reset, member);

        verifyEmailCode(reset.getEmail(),reset.getCode());

        ResponseResetPassword resetPassword = setResponseResetPassword(member);

        return resetPassword;
    }

    private static ResponseResetPassword setResponseResetPassword(Member member) {
        ResponseResetPassword resetPassword = new ResponseResetPassword();
        resetPassword.setUserId(member.getUserId());
        return resetPassword;
    }

    private static void checkUserId(PasswordReset reset, Member member) {
        if(!member.getUserId().equals(reset.getUserId())){
            throw new MemberMismatchException("회원 정보가 일치하지 않습니다.");
        }
    }

    private Member returnMemberByEmail(String email) {
        Optional<Member> emailOptional = memberRepository.findByEmail(email);
        return emailOptional.orElseThrow(() ->
                new EmailNotFoundException("이메일 존재하지 않습니다."));
    }

    public ResponseFindId findId(FindIdRequest request){

        verifyEmailCode(request.getEmail(),request.getCode());

        Member member = returnMemberByEmail(request.getEmail());

        return setResponseFindId(member);

    }

    private static ResponseFindId setResponseFindId(Member member) {
        ResponseFindId responseFindId = new ResponseFindId();
        responseFindId.setUserId(member.getUserId());
        return responseFindId;
    }

    public void passwordReset(CheckPasswordRequest request) {

        checkPassword(request.getPassword(), request.getCheckPassword());

        Member member = returnMember(request.getUserId());

        member.setPassword(encoder.encode(request.getPassword()));

        memberRepository.save(member);

    }

    private void checkPassword(String password, String checkPassword){
        if(!password.equals(checkPassword)){
            throw new PasswordMismatch("비밀번호가 일치하지 않습니다.");
        }
    }

    public void deleteMember(DeleteMemberRequest deleteMemberRequest){

        Member member = findById(deleteMemberRequest.getMemberId());

        List<String> list = extractScheduleId(member);

        memberRepository.deleteFriendRequestReceiverByMemberId(member);
        memberRepository.deleteFriendFriendsByMemberId(member);
        memberRepository.deleteFriendGroupByMemberId(member);
        memberRepository.deleteFriendGroupMemberByMemberId(member);
        memberScheduleRepository.deleteByAllId(list);
        scheduleRepository.deleteByCreator(member);
        memberRepository.deleteFriendOwnerByMemberId(member);
        memberRepository.deleteSearchHistoryByMemberId(member);
        memberRepository.deleteFriendRequestSenderIdByMemberId(member);
        fcmTokenRepository.deleteByMemberId(member.getId());
        refreshTokenRepository.deleteByMemberId(member.getId());
        memberInfoRepository.deleteByMemberId(member.getId());
        memberRepository.deleteById(member.getId());

    }

    private List<String> extractScheduleId(Member member) {
        return scheduleRepository.findSchedulesByMember(member)
                .stream()
                .map(Schedule::getId)
                .collect(Collectors.toList());
    }

    public ResponseMember getDetailMember(String memberId){
        Member member = findById(memberId);

        return setResponseMember(member);
    }

    private Member findById(String memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        return memberOptional.orElseThrow(() ->
                new MemberIdNotFoundException("존재하지 않는 memberId 입니다."));
    }

    private static ResponseMember setResponseMember(Member member) {
        ResponseMember responseMember = new ResponseMember();
        responseMember.setUserName(member.getUserName());
        responseMember.setUserId(member.getUserId());
        responseMember.setEmail(member.getEmail());
        responseMember.setProfileImage(member.getProfileImage());
        return responseMember;
    }

    public void modifyMyPage(MultipartFile multipartFile,String memberId,String newId,String userName) throws Exception {
        //멤버 찾기
        Member member = findById(memberId);
        //아이디 변경
        modifyUserId(newId, member);
        //프로필 사진
        modifyProfileImage(multipartFile, member);
        //userName 변경
        modifyUserName(userName, member);
        //저장
        memberRepository.save(member);

    }

    private static void modifyUserName(String userName, Member member) {
        if(userName !=null){
            member.setUserName(userName);
        }
    }

    private void modifyProfileImage(MultipartFile multipartFile, Member member) throws Exception {
        if(multipartFile !=null){
            String upload = awsS3Service.upload(multipartFile);
            member.setProfileImage(upload);
        }
    }

    private void modifyUserId(String newId, Member member) {
        if(newId !=null){
            //아이디 중복 체크
            memberRepository.findByUserId(newId)
                    .ifPresent(user -> {
                        throw new UserIdDuplicatedException("이미 존재 하는 아이디 입니다.");
                    });

            member.setUserId(newId);
        }
    }

    public ResponseMemberByUserId getDetailMemberByUserId(String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Member member = returnMember(userId);

        if(member.getId().equals(name)){
            throw new SelfSearchException("본인의 ID는 검색할 수 없습니다.");
        }

        return setResponseMemberByUserId(member);
    }

    private static ResponseMemberByUserId setResponseMemberByUserId(Member member) {
        ResponseMemberByUserId responseMemberByUserId = new ResponseMemberByUserId();
        responseMemberByUserId.setUserName(member.getUserName());
        responseMemberByUserId.setMemberId(member.getId());
        responseMemberByUserId.setProfileImage(member.getProfileImage());
        return responseMemberByUserId;
    }

    public void logout(String memberId){
        fcmTokenRepository.deleteByMemberId(memberId);

    }
}