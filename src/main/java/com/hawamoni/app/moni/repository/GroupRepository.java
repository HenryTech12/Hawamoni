package com.hawamoni.app.moni.repository;

import com.hawamoni.app.moni.model.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupModel,Long> {
}
