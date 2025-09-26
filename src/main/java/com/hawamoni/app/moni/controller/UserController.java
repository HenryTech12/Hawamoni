package com.hawamoni.app.moni.controller;

import com.hawamoni.app.moni.dto.UserDTO;
import com.hawamoni.app.moni.request.LoginRequest;
import com.hawamoni.app.moni.request.RefreshTokenRequest;
import com.hawamoni.app.moni.request.UserProfileRequest;
import com.hawamoni.app.moni.request.WalletLoginRequest;
import com.hawamoni.app.moni.response.UserResponse;
import com.hawamoni.app.moni.service.UserProfileService;
import com.hawamoni.app.moni.service.UserService;
import com.hawamoni.app.moni.tokens.JwtToken;
import com.hawamoni.app.moni.tokens.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/moni")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;


    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserDTO userDTO) {
        System.out.println(userDTO);
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.OK);
    }



    @PostMapping("/token/refresh")
    public ResponseEntity<JwtToken> getJwtToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return new ResponseEntity<>(userService.generateAccessToken(refreshTokenRequest),HttpStatus.OK);
    }

    @PostMapping("/auth/login")
    public void authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {

    }

    @PostMapping("/auth/nonce")
    public ResponseEntity<Map<String,String>> getAuthNonce(@RequestBody String walletAddress) {
        return new ResponseEntity<>(userService.generateNonce(walletAddress),HttpStatus.OK);
    }

    @PostMapping("/auth/verify")
    public ResponseEntity<Map<String,Object>> verifyNonce(@RequestBody @Valid WalletLoginRequest walletLoginRequest) {
        //return new ResponseEntity<>(userService.verifyNonceToken(walletLoginRequest),HttpStatus.OK);
        return null;
    }

    @PostMapping("/profile/create")
    public ResponseEntity<Map<String,Object>> createUserProfile(@RequestBody @Valid UserProfileRequest userProfileRequest,HttpServletRequest request) {
        return new ResponseEntity<>(userProfileService.createUserProfile(userProfileRequest,extractToken(request)), HttpStatus.OK);
    }

    @PutMapping("/user/me")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserDTO userDTO,HttpServletRequest request) {
        return new ResponseEntity<>(userService.updateUser(userDTO, extractToken(request)),HttpStatus.OK);
    }

    @DeleteMapping("/user/me")
    public ResponseEntity<Map<String,Object>> deleteUser(HttpServletRequest request) {
        return new ResponseEntity<>(userService.deleteUser(extractToken(request)),HttpStatus.OK);
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
        return new ResponseEntity<>(userService.getLoggedInUser(extractToken(request)),HttpStatus.OK);
    }

    @GetMapping("/profile/me")
    public ResponseEntity<UserProfileRequest> getUserProfile(HttpServletRequest request) {
        return new ResponseEntity<>(userProfileService.getUserProfile(extractToken(request)),HttpStatus.OK);
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null) {
            return header.substring(7);
        }
        return null;
    }

}
