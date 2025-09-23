package com.hawamoni.app.moni.service;

import com.hawamoni.app.moni.dto.GroupDTO;
import com.hawamoni.app.moni.exceptions.GroupDataNotFound;
import com.hawamoni.app.moni.exceptions.UserDataNotFound;
import com.hawamoni.app.moni.mappers.GroupMapper;
import com.hawamoni.app.moni.model.GroupModel;
import com.hawamoni.app.moni.model.UserModel;
import com.hawamoni.app.moni.repository.GroupRepository;
import com.hawamoni.app.moni.repository.UserRepository;
import com.hawamoni.app.moni.response.GroupResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public GroupResponse createGroup(GroupDTO groupDTO, String token) {
        GroupResponse groupResponse = null;
        if(!Objects.isNull(groupDTO)) {
            GroupModel groupModel = groupMapper.convertToModel(groupDTO);

            UserModel userModel = userRepository.
                    findByEmail(jwtService.extractEmail(token))
                            .orElseThrow(() -> new UserDataNotFound("user not found"));

            groupModel.setUserModel(userModel);

            groupRepository.save(groupModel);
            log.info("group model data added to database");
            groupResponse = new GroupResponse(groupModel.getGroupId(),userModel.getId(),
                    "Group successfully created.");
        }
        return groupResponse;
    }

    public GroupDTO getGroupByID(Long id) {
        return groupRepository.findById(id)
                .map(groupMapper::convertToDTO)
                .orElseThrow(() -> new GroupDataNotFound(String.format("group data with id: %d not found",id)));
    }
}
