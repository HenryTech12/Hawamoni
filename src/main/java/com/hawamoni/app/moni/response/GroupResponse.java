package com.hawamoni.app.moni.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupResponse {

    private Long groupId;
    private Long creator; //user id
    private String groupName;
}
