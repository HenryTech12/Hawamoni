package com.hawamoni.app.moni.controller;

import com.hawamoni.app.moni.dto.UserDTO;
import com.hawamoni.app.moni.request.LoginRequest;
import com.hawamoni.app.moni.request.RefreshTokenRequest;
import com.hawamoni.app.moni.response.UserResponse;
import com.hawamoni.app.moni.service.UserService;
import com.hawamoni.app.moni.tokens.JwtToken;
import com.hawamoni.app.moni.tokens.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/moni")
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.OK);
    }

    @PostMapping("/oauth/create")
    public ResponseEntity<UserResponse> createUserViaOauth() {
        System.out.println("hello");
        return null;
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<JwtToken> getJwtToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ResponseEntity<>(userService.generateAccessToken(refreshTokenRequest),HttpStatus.OK);
    }

    @PostMapping("/auth/login")
    public void authenticateUser(@RequestBody LoginRequest loginRequest) {

    }

    @GetMapping("/auth/google")
    public void continueWithGoogle() {
        System.out.println("yokose");
    }
}
