package com.hawamoni.app.moni.request;

public record GroupRequest(
        String groupName, String description, double deposit, String email
) {
}
