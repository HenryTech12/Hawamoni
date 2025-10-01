package com.hawamoni.app.moni.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class GroupModel {

    @ManyToOne(fetch = FetchType.EAGER)
    private UserModel userModel;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    private String pda_pubkey;
    private String name;
    private String description;
    private String treasury_pubkey;
    private int approvals_count;
    private String approvals_required;
    private String created_by;
    private String created_at;

}
