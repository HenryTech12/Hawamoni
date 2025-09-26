package com.hawamoni.app.moni.repository;

import com.hawamoni.app.moni.model.UserModel;
import com.hawamoni.app.moni.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

    Optional<UserProfile> findByUserModel(UserModel userModel);
}
