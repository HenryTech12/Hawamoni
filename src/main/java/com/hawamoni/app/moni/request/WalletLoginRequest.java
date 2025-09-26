package com.hawamoni.app.moni.request;

import jakarta.validation.constraints.NotNull;

public record WalletLoginRequest(
        @NotNull(message = "nonce can't be null") String nonce,
        @NotNull(message = "walletAddress can't be null") String walletAddress,
        @NotNull(message = "invalid signature, can't be null") String signature
) {
}
