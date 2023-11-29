package com.example.whereareyou.friendGroupMember.service;

import com.example.whereareyou.friendGroup.domain.FriendGroup;
import com.example.whereareyou.friendGroup.exception.FriendGroupNotFoundException;
import com.example.whereareyou.friendGroup.repository.FriendGroupRepository;
import com.example.whereareyou.friendGroupMember.domain.FriendGroupMember;
import com.example.whereareyou.friendGroupMember.repository.FriendGroupMemberRepository;
import com.example.whereareyou.friendGroupMember.request.RequestModifyGroupMember;
import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.member.exception.UserNotFoundException;
import com.example.whereareyou.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendGroupMemberService {
    private final FriendGroupMemberRepository friendGroupMemberRepository;
    private final FriendGroupRepository friendGroupRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public FriendGroupMemberService(FriendGroupMemberRepository friendGroupMemberRepository,
                                    FriendGroupRepository friendGroupRepository,
                                    MemberRepository memberRepository) {
        this.friendGroupMemberRepository = friendGroupMemberRepository;
        this.friendGroupRepository = friendGroupRepository;
        this.memberRepository = memberRepository;
    }

    public void modifyGroupMember(RequestModifyGroupMember requestModifyGroupMember) {
        memberRepository.findById(requestModifyGroupMember.getMemberId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));
        FriendGroup friendGroup = friendGroupRepository.findById(requestModifyGroupMember.getFriendGroupId())
                .orElseThrow(() -> new FriendGroupNotFoundException("존재하지 않는 friendGroupId입니다."));
        List<Member> friends = requestModifyGroupMember.getFriendIds().stream()
                .map(id -> memberRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다.")))
                .collect(Collectors.toList());

        List<FriendGroupMember> currentMembers = friendGroupMemberRepository.findByFriendGroup(friendGroup);
        List<String> currentMemberIds = currentMembers.stream()
                .map(FriendGroupMember::getMember)
                .map(Member::getId)
                .collect(Collectors.toList());

        List<String> membersToAdd = friends.stream()
                .map(Member::getId)
                .filter(id -> !currentMemberIds.contains(id))
                .collect(Collectors.toList());
        addNewMembers(friendGroup, membersToAdd);

        List<FriendGroupMember> membersToRemove = currentMembers.stream()
                .filter(member -> !requestModifyGroupMember.getFriendIds().contains(member.getMember().getId()))
                .collect(Collectors.toList());
        removeMembers(membersToRemove);
    }

    private void addNewMembers(FriendGroup friendGroup, List<String> memberIds) {
        memberIds.forEach(memberId -> {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

            FriendGroupMember newMember = FriendGroupMember.builder()
                    .friendGroup(friendGroup)
                    .member(member)
                    .build();

            friendGroupMemberRepository.save(newMember);
        });
    }

    private void removeMembers(List<FriendGroupMember> members) {
        friendGroupMemberRepository.deleteAll(members);
    }
}
