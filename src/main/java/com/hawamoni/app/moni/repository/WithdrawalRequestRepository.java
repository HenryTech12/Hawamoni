package com.hawamoni.app.moni.repository;

import com.hawamoni.app.moni.model.WithdrawalRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequestModel,Long> {
}
