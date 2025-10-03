package com.hawamoni.app.moni.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
public class GroupModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    private Long creator;
    private String pda_pubkey;
    private String groupName;
    private String description;
    private String treasury_pubkey;
    private int approvalsThreshold;
    private String approvalsRequired;
    private String pdaAddress;
    private double treasuryBalance;
    private int totalMembers;
    private String createdAt;
    @ManyToOne
    private UserModel user;
}
