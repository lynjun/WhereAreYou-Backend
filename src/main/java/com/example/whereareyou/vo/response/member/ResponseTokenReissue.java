package com.example.whereareyou.vo.response.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseTokenReissue {

    private String accessToken;
    private String refreshToken;
    private String memberId;
}
