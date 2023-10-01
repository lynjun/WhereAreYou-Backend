package com.example.whereareyou.vo.response.schedule;

import com.example.whereareyou.dto.BriefDateScheduleDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * packageName    : project.whereareyou.vo.response.schedule
 * fileName       : ResponseBriefDateSchedule
 * author         : pjh57
 * date           : 2023-09-17
 * description    : 일별 간략 정보 Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-17        pjh57       최초 생성
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBriefDateSchedule {
    private List<BriefDateScheduleDTO> briefDateScheduleDTOList;
}
