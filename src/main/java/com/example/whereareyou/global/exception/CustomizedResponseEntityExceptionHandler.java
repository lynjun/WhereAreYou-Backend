package com.example.whereareyou.global.exception;

import com.example.whereareyou.friend.exception.AlreadyFriendsException;
import com.example.whereareyou.friend.exception.AlreadySent;
import com.example.whereareyou.friend.exception.FriendRequestNotFoundException;
import com.example.whereareyou.friendGroup.exception.FriendGroupNotFoundException;
import com.example.whereareyou.friendGroup.exception.GroupMemberEmptyException;
import com.example.whereareyou.friendGroup.exception.GroupOwnerMismatchException;
import com.example.whereareyou.friendGroupMember.exception.MemberNotInGroupException;
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

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "G001");

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> userNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "C004");

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberIdCannotBeInFriendListException.class)
    public final ResponseEntity<Object> memberIdCannotBeInFriendListException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A008");

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidYearOrMonthOrDateException.class)
    public final ResponseEntity<Object> invalidYearOrMonthOrDateException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A009");

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UpdateQueryException.class)
    public final ResponseEntity<Object> updateQueryException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "G002");

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public final ResponseEntity<Object> scheduleNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "C001");

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotCreatedScheduleByMemberException.class)
    public final ResponseEntity<Object> notCreatedScheduleByMemberException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "C009");

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtTokenMismatchException.class)
    public final ResponseEntity<Object> jwtTokenMismatchException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false),"H001");

        return new ResponseEntity(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserIdDuplicatedException.class)
    public final ResponseEntity<Object> UserIdDuplicatedException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "D001");

        return new ResponseEntity(exceptionResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailDuplicatedException.class)
    public final ResponseEntity<Object> emailDuplicated(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "D002");

        return new ResponseEntity(exceptionResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCode.class)
    public final ResponseEntity<Object> invalidCode(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A003");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenNotFound.class)
    public final ResponseEntity<Object> TokenNotFound(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "C006");

        return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwt.class)
    public final ResponseEntity<Object> ExpiredJwt(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "B003");

        return new ResponseEntity(exceptionResponse,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PasswordMismatch.class)
    public final ResponseEntity<Object> PasswordMismatch(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "B002");

        return new ResponseEntity(exceptionResponse,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public final ResponseEntity<Object> EmailNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "C005");

        return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SearchHistoryNotFoundException.class)
    public final ResponseEntity<Object> searchHistoryNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "C007");

        return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsedTokenException.class)
    public final ResponseEntity<Object> usedTokenException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "E001");

        return new ResponseEntity(exceptionResponse,HttpStatus.GONE);
    }

    @ExceptionHandler(FriendRequestNotFoundException.class)
    public final ResponseEntity<Object> friendRequestNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "C008");

        return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberMismatchException.class)
    public final ResponseEntity<Object> memberMismatchException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A012");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public final ResponseEntity<Object> invalidEmailException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false),"A020");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadySent.class)
    public final ResponseEntity<Object> alreadySentException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A006");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyFriendsException.class)
    public final ResponseEntity<Object> alreadyFriendsException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A013");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreatorCannotRefuseSchedule.class)
    public final ResponseEntity<Object> creatorCannotRefuseSchedule(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A014");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestTimeException.class)
    public final ResponseEntity<Object> invalidRequestTimeException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A019");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FriendGroupNotFoundException.class)
    public final ResponseEntity<Object> friendGroupNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A015");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GroupOwnerMismatchException.class)
    public final ResponseEntity<Object> groupOwnerMismatchException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A016");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GroupMemberEmptyException.class)
    public final ResponseEntity<Object> groupMemberEmptyException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A017");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotInGroupException.class)
    public final ResponseEntity<Object> memberNotInGroupException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "A018");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SelfSearchException.class)
    public final ResponseEntity<Object> selfSearchException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false),"A021");

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberIdNotFoundException.class)
    public final ResponseEntity<Object> MemberIdNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "C002");

        return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
    }
}
