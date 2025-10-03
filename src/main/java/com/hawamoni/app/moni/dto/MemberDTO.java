package com.hawamoni.app.moni.dto;

import lombok.Data;

@Data
public class MemberDTO
{

    private Long memberId;
    private String email;
    private Long userId;
    private Long groupId;
    private String firstName;
    private String lastName;
    private MemberRole role;
    private String joinedAt;
    private boolean isActive;
}
