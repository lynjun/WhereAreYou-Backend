package com.example.whereareyou.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseLogin {

    private String accessToken;
    private String refreshToken;
    private String memberId;
}
