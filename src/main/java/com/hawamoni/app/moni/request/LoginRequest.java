package com.hawamoni.app.moni.request;

import lombok.Builder;

@Builder
public record LoginRequest(
        String email, String password
) {
}
