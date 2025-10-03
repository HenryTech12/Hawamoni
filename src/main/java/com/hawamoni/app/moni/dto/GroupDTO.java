package com.hawamoni.app.moni.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long groupId;
    private Long creator;
    @NotNull(message = "name cannot be null")
    private String groupName;
    @NotNull(message = "description cannot be null")
    private String description;
    private String treasury_pubkey;
    private int approvalsThreshold;
    private String approvalsRequired;
    private String pdaAddress;
    private double treasuryBalance;
    private int totalMembers;
    private String createdAt;

}
