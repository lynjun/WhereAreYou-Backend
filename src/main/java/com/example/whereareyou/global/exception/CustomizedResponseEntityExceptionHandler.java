package com.example.whereareyou.global.exception;

import com.example.whereareyou.friend.exception.AlreadyFriendsException;
import com.example.whereareyou.friend.exception.AlreadySent;
import com.example.whereareyou.friend.exception.FriendRequestNotFoundException;
import com.example.whereareyou.friendGroup.exception.FriendGroupNotFoundException;
import com.example.whereareyou.friendGroup.exception.GroupOwnerMismatchException;
import com.example.whereareyou.member.exception.*;
import com.example.whereareyou.memberInfo.exception.InvalidRequestTimeException;
import com.example.whereareyou.memberSchedule.exception.CreatorCannotRefuseSchedule;
import com.example.whereareyou.refreshToken.exception.ExpiredJwt;
import com.example.whereareyou.refreshToken.exception.JwtTokenMismatchException;
import com.example.whereareyou.refreshToken.exception.TokenNotFound;
import com.example.whereareyou.refreshToken.exception.UsedTokenException;
import com.example.whereareyou.schedule.exception.*;
import com.example.whereareyou.searchHistory.exception.SearchHistoryNotFoundException;
import com.example.whereareyou.global.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * packageName    : project.whereareyou.exception.customexception
 * fileName       : ScheduleController
 * author         : pjh57
 * date           : 2023-09-16
 * description    : CustomizedResponseEntityExceptionHandler
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> userNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FriendListNotFoundException.class)
    public final ResponseEntity<Object> friendListNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberIdCannotBeInFriendListException.class)
    public final ResponseEntity<Object> memberIdCannotBeInFriendListException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidYearOrMonthOrDateException.class)
    public final ResponseEntity<Object> invalidYearOrMonthOrDateException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UpdateQueryException.class)
    public final ResponseEntity<Object> updateQueryException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public final ResponseEntity<Object> scheduleNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotCreatedScheduleByMemberException.class)
    public final ResponseEntity<Object> notCreatedScheduleByMemberException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtTokenMismatchException.class)
    public final ResponseEntity<Object> jwtTokenMismatchException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserIdDuplicatedException.class)
    public final ResponseEntity<Object> UserIdDuplicatedException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailDuplicatedException.class)
    public final ResponseEntity<Object> emailDuplicated(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCode.class)
    public final ResponseEntity<Object> invalidCode(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenNotFound.class)
    public final ResponseEntity<Object> TokenNotFound(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwt.class)
    public final ResponseEntity<Object> ExpiredJwt(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PasswordMismatch.class)
    public final ResponseEntity<Object> PasswordMismatch(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public final ResponseEntity<Object> EmailNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SearchHistoryNotFoundException.class)
    public final ResponseEntity<Object> searchHistoryNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsedTokenException.class)
    public final ResponseEntity<Object> usedTokenException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.GONE);
    }

    @ExceptionHandler(FriendRequestNotFoundException.class)
    public final ResponseEntity<Object> friendRequestNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberMismatchException.class)
    public final ResponseEntity<Object> memberMismatchException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public final ResponseEntity<Object> invalidEmailException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadySent.class)
    public final ResponseEntity<Object> alreadySentException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyFriendsException.class)
    public final ResponseEntity<Object> alreadyFriendsException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreatorCannotRefuseSchedule.class)
    public final ResponseEntity<Object> creatorCannotRefuseSchedule(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestTimeException.class)
    public final ResponseEntity<Object> invalidRequestTimeException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FriendGroupNotFoundException.class)
    public final ResponseEntity<Object> friendGroupNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GroupOwnerMismatchException.class)
    public final ResponseEntity<Object> groupOwnerMismatchException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }
}
