package com.hawamoni.app.moni.mappers;

import com.hawamoni.app.moni.dto.GroupDTO;
import com.hawamoni.app.moni.model.GroupModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GroupMapper {

    @Autowired
    private ModelMapper mapper;

    public GroupDTO convertToDTO(GroupModel groupModel) {
        if(!Objects.isNull(groupModel))
            return mapper.map(groupModel, GroupDTO.class);
        else
            return null;
    }

    public GroupModel convertToModel(GroupDTO groupDTO) {
        if(!Objects.isNull(groupDTO))
            return mapper.map(groupDTO, GroupModel.class);
        else
            return null;
    }
}
