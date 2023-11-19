package com.example.whereareyou.friendGroup.service;

import com.example.whereareyou.friendGroup.domain.FriendGroup;
import com.example.whereareyou.friendGroup.repository.FriendGroupRepository;
import com.example.whereareyou.friendGroup.request.RequestCreateGroup;
import com.example.whereareyou.friendGroup.response.ResponseCreateGroup;
import com.example.whereareyou.friendGroupMember.domain.FriendGroupMember;
import com.example.whereareyou.friendGroupMember.repository.FriendGroupMemberRepository;
import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.member.exception.UserNotFoundException;
import com.example.whereareyou.member.repository.MemberRepository;
import com.example.whereareyou.memberSchedule.domain.MemberSchedule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.whereareyou.friendGroup.service
 * fileName       : FriendGroupService
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 Service
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Service
@Transactional
public class FriendGroupService {
    private final MemberRepository memberRepository;
    private final FriendGroupRepository friendGroupRepository;
    private final FriendGroupMemberRepository friendGroupMemberRepository;

    @Autowired
    public FriendGroupService(MemberRepository memberRepository, FriendGroupRepository friendGroupRepository, FriendGroupMemberRepository friendGroupMemberRepository) {
        this.memberRepository = memberRepository;
        this.friendGroupRepository = friendGroupRepository;
        this.friendGroupMemberRepository = friendGroupMemberRepository;
    }

    public ResponseCreateGroup createGroup(RequestCreateGroup requestCreateGroup){
        Member owner = memberRepository.findById(requestCreateGroup.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        for(String id : requestCreateGroup.getGroupMemberId()) {
            memberRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));
        }

        // FriendGroup 생성 및 저장
        FriendGroup friendGroup = FriendGroup.builder()
                .owner(owner)
                .build();
        friendGroup = friendGroupRepository.save(friendGroup);

        // 그룹 멤버들 추가
        for (String memberId : requestCreateGroup.getGroupMemberId()) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

            FriendGroupMember friendGroupMember = FriendGroupMember.builder()
                    .friendGroup(friendGroup)
                    .member(member)
                    .build();
            friendGroupMemberRepository.save(friendGroupMember);
        }

        ResponseCreateGroup responseCreateGroup = new ResponseCreateGroup();
        responseCreateGroup.setGroupId(friendGroup.getId());
        return responseCreateGroup;
    }
}
