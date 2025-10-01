package com.hawamoni.app.moni.service;

import com.hawamoni.app.moni.dto.UserDTO;
import com.hawamoni.app.moni.dto.UserRole;
import com.hawamoni.app.moni.exceptions.UserDataNotFound;
import com.hawamoni.app.moni.mappers.UserMapper;
import com.hawamoni.app.moni.model.UserModel;
import com.hawamoni.app.moni.repository.UserRepository;
import com.hawamoni.app.moni.request.RefreshTokenRequest;
import com.hawamoni.app.moni.response.UserResponse;
import com.hawamoni.app.moni.tokens.AccessToken;
import com.hawamoni.app.moni.tokens.JwtToken;
import com.hawamoni.app.moni.tokens.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    @Value("${OAUTH_ENDPOINT}")
    private String OAUTH_ENDPOINT;
    @Value("${OAUTH_CLIENT_ID}")
    private String CLIENT_ID;
    @Value("${OAUTH_REDIRECT_URI}")
    private String REDIRECT_URI;
    @Autowired
    private GoogleAuthService googleAuthService;

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

    public UserResponse updateUser(UserDTO userDTO) {
        String email = userDTO.getEmail();
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

    public Map<String, Object> deleteUser(String email) {
        Map<String,Object> data = new HashMap<>();
        userRepository.deleteByEmail(email);
        data.put("success",true);
        data.put("message", "User Data Deleted Successfully");
        data.put("email",email);
        return data;
    }

    public UserDTO getLoggedInUser(String token) {
        return getUserByEmail(jwtService.extractEmail(token));
    }

    public JwtToken saveUserByOauth(String token) {
        JwtToken jwtToken = null;
        try {
            var payload = googleAuthService.verify(token);
            if (payload == null) {
                throw new RuntimeException("Invalid ID token");
            }

            String email = payload.getEmail();
            String name = (String) payload.get("name");

            System.out.println(name);

            UserDTO userDTO = UserDTO.builder()
                    .email(email)
                    .role(UserRole.USER)
                    .build();

            System.out.println(userDTO);
            AccessToken accessToken = jwtService.generateAccessKey(userDTO);
            RefreshToken refreshToken = jwtService.generateRefreshToken(userDTO);

            jwtToken = new JwtToken(refreshToken.getRefresh_token(),refreshToken.getRefresh_expiry_time(),accessToken.getAccess_token(),accessToken.getAccess_expiry_time());
        }
        catch(Exception e) {
            log.info("an error occurred!!!: {}",e.getMessage());
            e.printStackTrace();
        }
        return jwtToken;
    }

    public Map<String, Object> getOauthInfo() {
        String oauthUrlTemplate = "%s?client_id=%s&redirect_uri=%s";
                //"https://accounts.google.com/o/oauth2/v2/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=openid%20email%20profile";
        String oauthUrl  =  String.format(oauthUrlTemplate,OAUTH_ENDPOINT,CLIENT_ID,REDIRECT_URI);
        String others = "&response_type=code&scope=openid%20email%20profile";
        Map<String,Object> data = new HashMap<>();
        data.put("oauthUrl", oauthUrl.concat(others));
        data.put("message","Call OAUTH URL to authenticate user");

        return data;
    }

    public UserDTO configureGoogleOauth(String email, String name, HttpServletRequest request) {

        UserDTO userDTO = userRepository.findByEmail(email)
                .map(userMapper::convertToDTO)
                .orElse(null);

        UserDTO newUser = null;
        if(userDTO == null) {
            String[] names = name.split(" ");
            String first_name = "";
            String last_name = "";

            if(names.length >= 2) {first_name = names[0];last_name = names[1];}
            else {first_name = name;}

            newUser = UserDTO.builder()
                    .first_name(first_name)
                    .last_name(last_name)
                    .role(UserRole.USER)
                    .password(UUID.randomUUID().toString().substring(8))
                    .email(email)
                    .build();
            request.setAttribute("withGoogle",true);
            createUser(newUser);
        }
        else {newUser = userDTO;}
        return newUser;
    }
}
