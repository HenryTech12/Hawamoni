package com.hawamoni.app.moni.tokens;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RefreshToken {

    private String refresh_token;
    private Date refresh_expiry_time;

}
