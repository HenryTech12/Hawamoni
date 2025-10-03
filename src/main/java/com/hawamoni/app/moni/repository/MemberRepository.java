package com.hawamoni.app.moni.repository;


import com.hawamoni.app.moni.model.MemberModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberModel, Long> {
}
