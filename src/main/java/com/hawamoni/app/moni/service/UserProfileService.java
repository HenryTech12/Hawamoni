package com.hawamoni.app.moni.service;

import com.hawamoni.app.moni.exceptions.UserDataNotFound;
import com.hawamoni.app.moni.mappers.UserProfileMapper;
import com.hawamoni.app.moni.model.UserModel;
import com.hawamoni.app.moni.model.UserProfile;
import com.hawamoni.app.moni.repository.UserProfileRepository;
import com.hawamoni.app.moni.repository.UserRepository;
import com.hawamoni.app.moni.request.UserProfileRequest;
import com.hawamoni.app.moni.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserProfileMapper userProfileMapper;


    public UserResponse updateUserProfile(UserProfileRequest userProfileRequest, String email) {
        return null;
    }

    public  Map<String,Object> createUserProfile(UserProfileRequest userProfileRequest, String token) {
        String email = jwtService.extractEmail(token);
        System.out.println(userProfileRequest);
        Map<String,Object> data = new HashMap<>();
        if(!Objects.isNull(userProfileRequest)) {
                UserModel userModel =getUserModel(email);
                UserProfile userProfile = userProfileMapper.convertToModel(userProfileRequest);
                userProfile.setUserModel(userModel);
                userProfile.setCreated_at(LocalDateTime.now());
                userProfileRepository.save(userProfile);

                data.put("userId", userModel.getId());
                data.put("profileId", userProfile.getProfileId());
                data.put("wallet_pubkey", userProfile.getWallet_pubkey());
                data.put("message", "user profile created");
                log.info("user profile created...");
        }
        return data;
    }

    public UserProfileRequest getUserProfile(String email) {

        Map<String,Object> data = new HashMap<>();
        if(email != null) {
            UserProfile userProfile = userProfileRepository.findByUserModel(getUserModel(email))
                    .orElseThrow(() -> new UserDataNotFound("User profile is not found"));

            return userProfileMapper.convertToRequest(userProfile);
        }
        return null;
    }

    public UserModel getUserModel(String email) {
              return  userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserDataNotFound(String.format("User with email: %s data not found", email)));
    }
}
