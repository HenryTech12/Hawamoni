package com.hawamoni.app.moni.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;

import java.util.List;

@Data
@Entity
@Builder
public class WithdrawalRequestModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long groupId;
    private int amount;
    private String recipientPubkey;
    private String reason;
    private String status;
    private int approvalsCount;
    private int approvalsRequired;
    private List<String> approvedBy;
    private String createdAt;
}
