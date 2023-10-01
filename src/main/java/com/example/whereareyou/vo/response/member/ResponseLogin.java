package com.example.whereareyou.vo.response.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseLogin {

    private String message;
    private String accessToken;
    private String refreshToken;


}
