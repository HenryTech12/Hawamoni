package com.hawamoni.app.moni.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {

    private Long groupId;
    private Long creator; //user id
    private String groupName;
    private String description;
    private int approvalThreshold;
    private int approvalsRequired;
    private String pdaAddress;
    private int treasuryBalance;
    private int pendingRequests;
    private String createdAt;
    private String lastActivity;
}
