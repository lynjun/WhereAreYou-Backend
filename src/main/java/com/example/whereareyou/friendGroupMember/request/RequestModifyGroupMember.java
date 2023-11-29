package com.example.whereareyou.friendGroupMember.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestModifyGroupMember {
    private String memberId;
    private String friendGroupId;
    private List<String> friendIds;
}
