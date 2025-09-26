package com.hawamoni.app.moni.mappers;

import com.hawamoni.app.moni.model.UserProfile;
import com.hawamoni.app.moni.request.UserProfileRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserProfileMapper {

    @Autowired
    private ModelMapper mapper;

    public UserProfile convertToModel(UserProfileRequest userProfileRequest) {
        if(!Objects.isNull(userProfileRequest))
            return mapper.map(userProfileRequest,UserProfile.class);
        else
            return null;
    }

    public UserProfileRequest convertToRequest(UserProfile userProfile) {
        if(!Objects.isNull(userProfile))
            return mapper.map(userProfile,UserProfileRequest.class);
        else
            return null;
    }
}
