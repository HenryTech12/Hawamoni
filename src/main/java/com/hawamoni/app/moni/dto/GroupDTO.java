package com.hawamoni.app.moni.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long groupId;
    @NotNull(message = "pda_pubkey cannot be null")
    private String pda_pubkey;
    @NotNull(message = "name cannot be null")
    private String name;
    @NotNull(message = "description cannot be null")
    private String description;
    @NotNull(message = "treasury_pubkey cannot be null")
    private String treasury_pubkey;
    private int approvals_count;
    private String approvals_required;
    private String created_by;
    private String created_at;
}
