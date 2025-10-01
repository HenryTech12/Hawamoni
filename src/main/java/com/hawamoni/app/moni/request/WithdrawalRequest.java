package com.hawamoni.app.moni.request;

public record WithdrawalRequest(
        String recipientPubkey, int amount, String tokenMint, String reason
) {
}
