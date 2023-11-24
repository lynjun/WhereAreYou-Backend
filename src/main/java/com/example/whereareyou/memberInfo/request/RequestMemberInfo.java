package com.example.whereareyou.memberInfo.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RequestMemberInfo {
    private String memberId;
    private Double latitude;
    private Double longitude;
}
