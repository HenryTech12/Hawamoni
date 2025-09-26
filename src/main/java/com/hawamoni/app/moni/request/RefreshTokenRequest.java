package com.hawamoni.app.moni.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public record RefreshTokenRequest(
        @NotNull(message = "email can't be null") String email,
        @NotNull(message = "refresh token can't be null") String refresh_token
) {

}
