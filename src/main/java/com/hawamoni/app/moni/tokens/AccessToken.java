package com.hawamoni.app.moni.tokens;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AccessToken {

    private String access_token;
    private Date access_expiry_time;
}
