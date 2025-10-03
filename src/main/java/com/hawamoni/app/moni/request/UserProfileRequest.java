package com.hawamoni.app.moni.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hawamoni.app.moni.dto.UserDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class UserProfileRequest {
    @NotNull(message = "wallet key cannot be null")
    private String walletAddress;
    @NotNull(message = "phone number cannot be null")
    private String phoneNum;
    private String email;
    private LocalDateTime created_at;
}

