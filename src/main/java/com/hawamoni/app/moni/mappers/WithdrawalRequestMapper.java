package com.hawamoni.app.moni.mappers;

import com.hawamoni.app.moni.dto.WithdrawalRequestDTO;
import com.hawamoni.app.moni.model.WithdrawalRequestModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class WithdrawalRequestMapper {

    @Autowired
    private ModelMapper mapper;

    public WithdrawalRequestDTO convertToDTO(WithdrawalRequestModel withdrawalRequestModel) {
        if(!Objects.isNull(withdrawalRequestModel))
            mapper.map(withdrawalRequestModel,WithdrawalRequestDTO.class);
        else
            return null;
        return null;
    }
}
