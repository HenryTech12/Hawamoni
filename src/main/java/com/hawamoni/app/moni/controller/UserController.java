package com.hawamoni.app.moni.controller;

import com.hawamoni.app.moni.dto.UserDTO;
import com.hawamoni.app.moni.request.LoginRequest;
import com.hawamoni.app.moni.request.RefreshTokenRequest;
import com.hawamoni.app.moni.response.UserResponse;
import com.hawamoni.app.moni.service.JwtService;
import com.hawamoni.app.moni.service.UserService;
import com.hawamoni.app.moni.tokens.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/moni")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;


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

    @PutMapping("/user/me")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserDTO userDTO,HttpServletRequest request) {
        return new ResponseEntity<>(userService.updateUser(userDTO),HttpStatus.OK);
    }

    @DeleteMapping("/user/me")
    public ResponseEntity<Map<String,Object>> deleteUser(HttpServletRequest request) {
        String token = extractToken(request);
        return new ResponseEntity<>(userService.deleteUser(jwtService.extractEmail(token)),HttpStatus.OK);
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
        return new ResponseEntity<>(userService.getLoggedInUser(extractToken(request)),HttpStatus.OK);
    }


    @GetMapping("/auth/google")
    public ResponseEntity<JwtToken> authUserViaOauth() {
        return null;
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null) {
            return header.substring(7);
        }
        return null;
    }

}
