package com.hawamoni.app.moni.repository;

import com.hawamoni.app.moni.model.GroupModel;
import com.hawamoni.app.moni.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupModel,Long> {

    List<GroupModel> findByCreator(Long creator);
}
