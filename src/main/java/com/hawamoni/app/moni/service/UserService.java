package com.hawamoni.app.moni.service;

import com.hawamoni.app.moni.dto.UserDTO;
import com.hawamoni.app.moni.dto.UserRole;
import com.hawamoni.app.moni.exceptions.UserDataNotFound;
import com.hawamoni.app.moni.mappers.UserMapper;
import com.hawamoni.app.moni.model.UserModel;
import com.hawamoni.app.moni.model.UserProfile;
import com.hawamoni.app.moni.repository.UserProfileRepository;
import com.hawamoni.app.moni.repository.UserRepository;
import com.hawamoni.app.moni.request.RefreshTokenRequest;
import com.hawamoni.app.moni.request.UserProfileRequest;
import com.hawamoni.app.moni.request.WalletLoginRequest;
import com.hawamoni.app.moni.response.UserResponse;
import com.hawamoni.app.moni.tokens.AccessToken;
import com.hawamoni.app.moni.tokens.JwtToken;
import com.hawamoni.app.moni.tokens.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.*;

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
                System.out.println(userModel.getId());
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

    public Map<String,String> generateNonce(String walletAddress) {
        Map<String, String> nonce = new HashMap<>();
        if(walletAddress != null) {
            nonce.put(walletAddress, UUID.randomUUID().toString());
        }
        return nonce;
    }
/*
    public Map<String,Object> verifyNonceToken(WalletLoginRequest walletLoginRequest) {
        Map<String,Object> data = new HashMap<>();
        if (!Objects.isNull(walletLoginRequest)) {
            try {
                String recoveredAddress = WalletAuthService.
                        recoverAddress(walletLoginRequest.nonce(), walletLoginRequest.signature());
                if (Objects.equals(recoveredAddress, walletLoginRequest.walletAddress())) {
                    data.put("token", jwtService.generateJwtTokenUsingWallet(walletLoginRequest.walletAddress()));
                    data.put("success",true);
                    return data;
                }
                else {
                    data.put("success",false);
                }
            } catch (SignatureException e) {
                log.info("an error occurred!!: {}", e.getMessage());
                data.put("success",false);
                data.put("error", e.getMessage());
                throw new RuntimeException(e);
            }

        }
        return null;
    }*/


    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::convertToDTO)
                .orElseThrow(() -> new UserDataNotFound(String.format("User with email: %s data not found",email)));
    }

    public UserModel getUserModel(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDataNotFound(String.format("User with email: %s data not found",email)));
    }

    public UserResponse updateUser(UserDTO userDTO, String token) {
        String email = jwtService.extractEmail(token);
        UserModel userModel = getUserModel(email);

        UserModel newUserModel = userMapper.convertToModel(userDTO);
        newUserModel.setRole(userModel.getRole());
        newUserModel.setEmail(email);
        newUserModel.setPassword(passwordEncoder.encode(newUserModel.getPassword()));
        newUserModel.setId(userModel.getId());

        userRepository.save(newUserModel);
        log.info("updated user with id: {}", userModel.getId());

        return UserResponse.builder()
                .email(email)
                .id(newUserModel.getId())
                .message("User data updated.")
                .build();
    }

    public Map<String, Object> deleteUser(String token) {
        Map<String,Object> data = new HashMap<>();
        String email = jwtService.extractEmail(token);
        userRepository.deleteByEmail(email);
        data.put("success",true);
        data.put("message", "User Data Deleted Successfully");
        data.put("email",email);
        return data;
    }

    public UserDTO getLoggedInUser(String token) {
        return getUserByEmail(jwtService.extractEmail(token));
    }
}
