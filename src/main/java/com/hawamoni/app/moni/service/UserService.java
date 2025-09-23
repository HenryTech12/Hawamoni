package com.hawamoni.app.moni.service;

import com.hawamoni.app.moni.dto.UserDTO;
import com.hawamoni.app.moni.exceptions.UserDataNotFound;
import com.hawamoni.app.moni.mappers.UserMapper;
import com.hawamoni.app.moni.model.UserModel;
import com.hawamoni.app.moni.repository.UserRepository;
import com.hawamoni.app.moni.request.RefreshTokenRequest;
import com.hawamoni.app.moni.response.UserResponse;
import com.hawamoni.app.moni.tokens.AccessToken;
import com.hawamoni.app.moni.tokens.JwtToken;
import com.hawamoni.app.moni.tokens.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public UserResponse createUser(UserDTO userDTO) {
        UserResponse userResponse = null;
        if(!Objects.isNull(userDTO)){
            if(!exists(userDTO)) {
                UserModel userModel = userMapper.convertToModel(userDTO);
                userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
                userRepository.save(userModel);
                log.info("user data saved to database.");
                userResponse = new UserResponse(userModel.getId(),
                        userModel.getEmail(),"User Details Saved.");
            }
            else {
                userResponse = new UserResponse(-1L,userDTO.getEmail(),"User already exists.");
            }
        }
        return userResponse;
    }

    public JwtToken generateAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String email = refreshTokenRequest.email();
        JwtToken jwtToken = null;
        if(email != null) {
            UserDTO userDTO = getUserByEmail(email);
            System.out.println("ID: "+userDTO.getId());
            AccessToken accessToken = jwtService.generateAccessKey(userDTO);
            Date refresh_expiry_date = jwtService.getExpiration(refreshTokenRequest.refresh_token());
            RefreshToken refreshToken = RefreshToken
                    .builder()
                    .refresh_token(refreshTokenRequest.refresh_token())
                    .refresh_expiry_time(refresh_expiry_date)
                    .build();
            jwtToken = new JwtToken(refreshToken.getRefresh_token(),refresh_expiry_date,accessToken.getAccess_token(),accessToken.getAccess_expiry_time());
        }
        return jwtToken;
    }

    public boolean exists(UserDTO userDTO) {
        return userRepository.findByEmail(userDTO.getEmail()).isPresent();
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::convertToDTO)
                .orElseThrow(() -> new UserDataNotFound(String.format("User with email: %s data not found",email)));
    }
}
