package com.hawamoni.app.moni.request;

import com.hawamoni.app.moni.dto.MemberRole;

public record UpdateMemberRequest(MemberRole role, boolean isActive) {
}
