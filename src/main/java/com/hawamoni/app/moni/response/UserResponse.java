package com.hawamoni.app.moni.response;

import com.hawamoni.app.moni.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {

    private Long id;
    private String email;
    private String message;
}
