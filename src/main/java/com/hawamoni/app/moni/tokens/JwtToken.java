package com.hawamoni.app.moni.tokens;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class JwtToken {

    private String refresh_token;
    private Date refresh_expiry_time;
    private String access_token;
    private Date access_expiry_time;
}
