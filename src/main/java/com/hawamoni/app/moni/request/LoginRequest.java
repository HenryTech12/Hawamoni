package com.hawamoni.app.moni.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotNull(message = "email cannot be null") String email, @NotNull(message = "password cannot be nul") String password
) {
}
