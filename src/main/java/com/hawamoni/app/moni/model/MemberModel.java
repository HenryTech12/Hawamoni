package com.hawamoni.app.moni.model;

import com.hawamoni.app.moni.dto.MemberRole;
import com.hawamoni.app.moni.dto.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MemberModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
