package com.hawamoni.app.moni.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull(message = "first name cannnot be null")
    private String first_name;
    @NotNull(message = "last name cannot be null")
    private String last_name;
    @NotNull(message = "email can't be null")
    @Email
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserRole role;
    private boolean signUpwithgoogle;
}
