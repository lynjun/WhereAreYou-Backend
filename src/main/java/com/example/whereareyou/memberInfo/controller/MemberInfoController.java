package com.example.whereareyou.memberInfo.controller;

import com.example.whereareyou.memberInfo.request.RequestGetMemberInfo;
import com.example.whereareyou.memberInfo.service.MemberInfoService;
import com.example.whereareyou.memberInfo.request.RequestMemberInfo;
import com.example.whereareyou.memberInfo.response.ResponseMemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 사용자 실시간 위도 경도
     *
     * @param requestGetMemberInfo the request get member info
     * @return the response entity
     */
    @PostMapping("/realTime")
    public ResponseEntity<List<ResponseMemberInfo>> getMemberInfo(@RequestBody RequestGetMemberInfo requestGetMemberInfo){
        List<ResponseMemberInfo> responseMemberInfo = memberInfoService.getMemberInfos(requestGetMemberInfo);

        return ResponseEntity.status(HttpStatus.OK).body(responseMemberInfo);
    }
}
