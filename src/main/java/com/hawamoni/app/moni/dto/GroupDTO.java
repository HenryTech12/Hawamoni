package com.hawamoni.app.moni.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GroupDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long groupId;
    private String pda_pubkey;
    private String name;
    private String description;
    private String treasury_pubkey;
    private String approvals_required;
    private String created_by;
    private String created_at;
}
