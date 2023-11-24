package com.example.whereareyou.global.constant;

public class ExceptionConstant {
    public static final String MEMBER_ID_CANNOT_BE_IN_FRIEND_LIST = "일정 친구 추가 시 본인의 ID는 들어갈 수 없습니다.";
    public static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "존재하지 않는 memberId입니다.";
    public static final String SCHEDULE_NOT_FOUND_EXCEPTION_MESSAGE = "존재하지 않는 scheduleId입니다.";
    public static final String SCHEDULE_CREATOR_MISMATCH_EXCEPTION_MESSAGE = "해당 schedule의 creator가 아닙니다.";
    public static final String UPDATE_QUERY_EXCEPTION_MESSAGE = "업데이트 실패";
    public static final int UPDATE_QUERY_EXCEPTION_SIZE = 0;
    public static final String INVALID_MONTH_EXCEPTION = "월이 올바르지 않습니다.";
    public static final String SCHEDULE_NOT_FOUND_OR_MEMBER_DIDNT_ACCEPT_EXCEPTION_MESSAGE = "존재하지 않는 scheduleId이거나 회원이 수락하지 않은 일정입니다.";
    public static final String MEMBER_NOT_IN_SCHEDULE_EXCEPTION_MESSAGE = "해당 Member는 Schedule에 존재하지 않습니다.";
    public static final String CREATOR_CANNOT_REFUSE_SCHEDULE = "스케줄의 생성자는 스케줄을 거부할 수 없습니다.";
}
