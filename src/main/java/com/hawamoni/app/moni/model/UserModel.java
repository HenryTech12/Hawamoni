package com.hawamoni.app.moni.model;

import com.hawamoni.app.moni.dto.GroupDTO;
import com.hawamoni.app.moni.dto.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String walletAddress;
    private String createdAt;
    private UserRole role;
    @OneToMany(mappedBy = "user")
    private List<GroupModel> groups;
}
