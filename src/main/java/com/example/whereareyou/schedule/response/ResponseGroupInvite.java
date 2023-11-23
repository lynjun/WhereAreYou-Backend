package com.example.whereareyou.schedule.response;

import com.example.whereareyou.schedule.dto.ScheduleInviteDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseGroupInvite {
    private List<ScheduleInviteDto> testList;
}