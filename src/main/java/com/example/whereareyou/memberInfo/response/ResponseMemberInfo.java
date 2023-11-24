package com.example.whereareyou.memberInfo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMemberInfo {
    private String memberId;
    private Double latitude;
    private Double longitude;
}
