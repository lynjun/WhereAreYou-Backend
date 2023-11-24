package com.example.whereareyou.memberInfo.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestGetMemberInfo {
    private List<String> memberId;
    private String scheduleId;
}
