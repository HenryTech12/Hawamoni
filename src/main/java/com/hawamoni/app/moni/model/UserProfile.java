package com.hawamoni.app.moni.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UserProfile {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    @OneToOne
    private UserModel userModel;
    private String wallet_pubkey;
    private String phone;
    private LocalDateTime created_at;
}
