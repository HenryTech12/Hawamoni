package com.hawamoni.app.moni.request;

import lombok.Data;

public record RefreshTokenRequest(
        String email, String refresh_token
) {

}
