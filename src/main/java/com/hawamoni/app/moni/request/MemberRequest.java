package com.hawamoni.app.moni.request;

import com.hawamoni.app.moni.dto.MemberRole;

public record MemberRequest(

    Long userId,String email,String firstName, String lastName, MemberRole role
) {
}
