package com.hawamoni.app.moni.mappers;

import com.hawamoni.app.moni.dto.MemberDTO;
import com.hawamoni.app.moni.model.MemberModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MemberMapper {

    @Autowired
    private ModelMapper mapper;

    public MemberDTO convertToDTO(MemberModel memberModel) {
        if(!Objects.isNull(memberModel))
            return mapper.map(memberModel,MemberDTO.class);
        else
            return null;
    }
}
