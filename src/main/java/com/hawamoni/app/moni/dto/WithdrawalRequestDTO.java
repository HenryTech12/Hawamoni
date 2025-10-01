package com.hawamoni.app.moni.dto;

import lombok.Data;

import java.util.List;

@Data
public class WithdrawalRequestDTO {

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
