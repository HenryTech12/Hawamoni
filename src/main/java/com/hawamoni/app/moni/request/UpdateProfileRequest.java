package com.hawamoni.app.moni.request;

public record UpdateProfileRequest(String firstName, String lastName,
                                   String phoneNumber, String walletAddress) {
}
