package com.example.whereareyou.friendGroup.service;

import com.example.whereareyou.friendGroup.domain.FriendGroup;
import com.example.whereareyou.friendGroup.exception.FriendGroupNotFoundException;
import com.example.whereareyou.friendGroup.exception.GroupOwnerMismatchException;
import com.example.whereareyou.friendGroup.repository.FriendGroupRepository;
import com.example.whereareyou.friendGroup.request.RequestCreateGroup;
import com.example.whereareyou.friendGroup.request.RequestDeleteGroup;
import com.example.whereareyou.friendGroup.request.RequestModifyGroupName;
import com.example.whereareyou.friendGroup.response.ResponseCreateGroup;
import com.example.whereareyou.friendGroup.response.ResponseGetGroup;
import com.example.whereareyou.friendGroupMember.domain.FriendGroupMember;
import com.example.whereareyou.friendGroupMember.dto.GroupMemberDto;
import com.example.whereareyou.friendGroupMember.repository.FriendGroupMemberRepository;
import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.member.exception.UserNotFoundException;
import com.example.whereareyou.member.repository.MemberRepository;
import com.example.whereareyou.memberSchedule.domain.MemberSchedule;
import com.example.whereareyou.schedule.exception.UpdateQueryException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    /**
     * Create group response create group.
     *
     * @param requestCreateGroup the request create group
     * @return the response create group
     */
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
                .name(requestCreateGroup.getName())
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

    /**
     * Gets group.
     *
     * @param ownerId the owner id
     * @return the group
     */
    public List<ResponseGetGroup> getGroup(String ownerId) {
        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        List<FriendGroup> friendGroups = friendGroupRepository.findByOwner(owner);
        List<ResponseGetGroup> responseGetGroups = new ArrayList<>();

        for (FriendGroup friendGroup : friendGroups) {
            List<FriendGroupMember> groupMembers = friendGroupMemberRepository.findByFriendGroup(friendGroup);

            List<GroupMemberDto> groupMemberDtos = groupMembers.stream()
                    .map(friendGroupMember -> new GroupMemberDto(
                            friendGroupMember.getMember().getId(),
                            friendGroupMember.getMember().getUserName()))
                    .collect(Collectors.toList());

            responseGetGroups.add(new ResponseGetGroup(friendGroup.getName(), groupMemberDtos));
        }

        return responseGetGroups;
    }

    /**
     * Delete group.
     *
     * @param requestDeleteGroup the request delete group
     */
    public void deleteGroup(RequestDeleteGroup requestDeleteGroup){
        Member owner = memberRepository.findById(requestDeleteGroup.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));
        FriendGroup friendGroup = friendGroupRepository.findById(requestDeleteGroup.getGroupId())
                .orElseThrow(() -> new FriendGroupNotFoundException("존재하지 않는 groupId입니다."));

        if(!friendGroup.getOwner().getId().equals(owner.getId())){
            throw new GroupOwnerMismatchException("해당 member가 생성한 그룹이 아닙니다.");
        }

        friendGroupRepository.delete(friendGroup);
    }

    public void modifyGroupName(RequestModifyGroupName requestModifyGroupName){
        Member owner = memberRepository.findById(requestModifyGroupName.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));
        FriendGroup friendGroup = friendGroupRepository.findById(requestModifyGroupName.getGroupId())
                .orElseThrow(() -> new FriendGroupNotFoundException("존재하지 않는 groupId입니다."));

        if(!friendGroup.getOwner().getId().equals(owner.getId())){
            throw new GroupOwnerMismatchException("해당 member가 생성한 그룹이 아닙니다.");
        }

        int update = friendGroupRepository.updateGroupName(requestModifyGroupName.getGroupId(), requestModifyGroupName.getName());

        if(update == 0){
            throw new UpdateQueryException("없데이트 실패");
        }
    }
}
