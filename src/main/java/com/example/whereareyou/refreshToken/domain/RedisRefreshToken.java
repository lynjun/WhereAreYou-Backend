package com.example.whereareyou.refreshToken.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;



@RedisHash(value = "refreshToken")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedisRefreshToken {

    @Id
    private String refreshToken;
    private String memberId;

}
