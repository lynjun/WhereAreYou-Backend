package com.example.whereareyou.controller;

import com.amazonaws.Response;
import com.example.whereareyou.domain.MemberInfo;
import com.example.whereareyou.service.MemberInfoService;
import com.example.whereareyou.vo.request.memberInfo.RequestMemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.example.whereareyou.controller
 * fileName       : MemberInfoController
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 위도 경도
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@RestController
@RequestMapping("/info")
public class MemberInfoController {

    private final MemberInfoService memberInfoService;

    @Autowired
    public MemberInfoController(MemberInfoService memberInfoService) {
        this.memberInfoService = memberInfoService;
    }


    /**
     * 위도/경도
     *
     * @param requestMemberInfo the request member info
     * @return the response entity
     */
    @PostMapping()
    public ResponseEntity<Boolean> setMemberInfo(@RequestBody RequestMemberInfo requestMemberInfo){
        memberInfoService.setMemberInfo(requestMemberInfo);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
