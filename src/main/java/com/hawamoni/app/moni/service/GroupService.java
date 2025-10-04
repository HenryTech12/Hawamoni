package com.hawamoni.app.moni.service;

import com.hawamoni.app.moni.dto.GroupDTO;
import com.hawamoni.app.moni.dto.MemberRole;
import com.hawamoni.app.moni.dto.UserDTO;
import com.hawamoni.app.moni.exceptions.GroupDataNotFound;
import com.hawamoni.app.moni.exceptions.UserDataNotFound;
import com.hawamoni.app.moni.mappers.GroupMapper;
import com.hawamoni.app.moni.model.GroupModel;
import com.hawamoni.app.moni.model.MemberModel;
import com.hawamoni.app.moni.model.UserModel;
import com.hawamoni.app.moni.notifications.Notification;
import com.hawamoni.app.moni.repository.GroupRepository;
import com.hawamoni.app.moni.repository.MemberRepository;
import com.hawamoni.app.moni.repository.UserRepository;
import com.hawamoni.app.moni.request.GroupRequest;
import com.hawamoni.app.moni.response.GroupResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public GroupResponse createGroup(GroupRequest groupRequest, String token) {
        GroupResponse groupResponse = null;
        if(!Objects.isNull(groupRequest)) {
            UserModel userModel = userRepository.
                    findByEmail(jwtService.extractEmail(token))
                            .orElseThrow(() -> new UserDataNotFound("user not found"));
            //validate user
            if(!Objects.isNull(userModel) && Objects.equals(groupRequest.email(),userModel.getEmail())) {
                GroupModel groupModel = new GroupModel();
                groupModel.setCreatedAt(LocalDateTime.now().toString());
                groupModel.setCreator(userModel.getId());
                groupModel.setGroupName(groupRequest.groupName());
                groupModel.setDescription(groupRequest.description());

                //save group to db
                groupRepository.save(groupModel);
                log.info("group model data added to database");

                //group details
                groupResponse = new GroupResponse();
                groupResponse.setGroupId(groupModel.getGroupId());
                groupResponse.setGroupName(groupModel.getGroupName());
                groupResponse.setCreatedAt(LocalDateTime.now().toString());
                groupResponse.setCreator(userModel.getId());
                groupResponse.setDescription(groupModel.getDescription());

                List<GroupModel> groups = setGroupList(userModel);
                groups.add(groupModel);

                //initialize group with blockchain deposit

                //send member invitation emails

                userModel.setGroups(groups);
                userRepository.save(userModel);

                messagingTemplate.convertAndSend("/moni/notification", new Notification("Group Created"));
            }
        }
        return groupResponse;
    }

    public List<GroupModel> setGroupList(UserModel userModel) {
        if(userModel.getGroups().isEmpty())
            return new ArrayList<>();
        else
            return userModel.getGroups();
    }


    public GroupDTO getGroupByID(Long id) {
        return groupRepository.findById(id)
                .map(groupMapper::convertToDTO)
                .orElseThrow(() -> new GroupDataNotFound(String.format("group data with id: %d not found",id)));
    }

    public void updateTotalMembers(int totalMembers, GroupModel groupModel) {
        groupModel.setTotalMembers(totalMembers);
        groupRepository.save(groupModel);
    }

    public List<GroupDTO> getGroups(String token) {
        String email = jwtService.extractEmail(token);
        UserDTO userDTO = userService.getUserByEmail(email);
        return groupRepository.findByCreator(userDTO.getId())
                .stream().map(groupMapper::convertToDTO).toList();
    }
}
