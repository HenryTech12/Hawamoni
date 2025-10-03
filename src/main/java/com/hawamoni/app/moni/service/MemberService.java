package com.hawamoni.app.moni.service;

import com.hawamoni.app.moni.dto.MemberDTO;
import com.hawamoni.app.moni.dto.MemberRole;
import com.hawamoni.app.moni.dto.UserRole;
import com.hawamoni.app.moni.exceptions.GroupDataNotFound;
import com.hawamoni.app.moni.exceptions.UserDataNotFound;
import com.hawamoni.app.moni.mappers.GroupMapper;
import com.hawamoni.app.moni.mappers.MemberMapper;
import com.hawamoni.app.moni.model.GroupModel;
import com.hawamoni.app.moni.model.MemberModel;
import com.hawamoni.app.moni.model.UserModel;
import com.hawamoni.app.moni.repository.GroupRepository;
import com.hawamoni.app.moni.repository.MemberRepository;
import com.hawamoni.app.moni.repository.UserRepository;
import com.hawamoni.app.moni.request.MemberRequest;
import com.hawamoni.app.moni.request.UpdateMemberRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@Slf4j
public class MemberService {


    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private GroupService groupService;

    public MemberDTO createMember(Long groupId, MemberRequest memberRequest, String token) {
        MemberDTO memberDTO = null;
        if(!Objects.isNull(memberRequest)) {
            String email = jwtService.extractEmail(token);

            UserModel userModel = userRepository.findByEmail(email)
                    .orElseThrow(() ->  new UserDataNotFound("User Not Found"));

            MemberModel memberModel = new MemberModel();
            memberModel.setUserId(userModel.getId());
            memberModel.setFirstName(memberRequest.firstName());
            memberModel.setLastName(memberRequest.lastName());
            memberModel.setActive(true);
            memberModel.setRole(MemberRole.MEMBER);
            memberModel.setJoinedAt(LocalDateTime.now().toString());
            memberModel.setGroupId(groupId);


            memberRepository.save(memberModel);

            GroupModel groupModel = groupRepository.findById(groupId)
                    .orElseThrow(() -> new GroupDataNotFound("Group Details Not Found"));

            int increaseTotalMember = groupModel.getTotalMembers() + 1;
            groupService.updateTotalMembers(increaseTotalMember,groupModel);
            log.info("member model saved to database");

            //trigger member added notification

            memberDTO = memberMapper.convertToDTO(memberModel);
        }
        return memberDTO;
    }

    public List<MemberDTO> getMembers(Long groupId) {
        return memberRepository.findAll()
                .stream().map(memberMapper::convertToDTO)
                .filter(data -> Objects.equals(data.getGroupId(),groupId))
                .toList();
    }

    public Map<String,Object> updateMemberDetails(Long groupId, Long memberId, UpdateMemberRequest request) {
        Map<String,Object> data = new HashMap<>();
        MemberModel memberModel = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserDataNotFound("No member data found"));
        memberModel.setActive(request.isActive());
        memberModel.setRole(request.role());

        if(Objects.equals(groupId,memberModel.getMemberId())) {
            GroupModel groupModel = groupRepository.findById(groupId)
                    .orElseThrow(() -> new GroupDataNotFound("No group data found"));

            memberRepository.save(memberModel);

            data.put("success",true);
            //send trigger notification

        }
        data.put("message", "member not found in group");

        return data;
    }

    public Map<String,Object> deleteMemberDetails(Long groupId, Long memberId) {
        Map<String,Object> data = new HashMap<>();
        MemberModel memberModel = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserDataNotFound("No member data found"));
        memberRepository.deleteById(memberModel.getMemberId());

        if(Objects.equals(groupId,memberModel.getMemberId())) {
            GroupModel groupModel = groupRepository.findById(groupId)
                    .orElseThrow(() -> new GroupDataNotFound("No group data found"));

            int value = groupModel.getTotalMembers() - 1;
            groupService.updateTotalMembers(value,groupModel);
            data.put("success", true);
        }

        data.put("message", "member not found in group");
        return data;
    }
}
