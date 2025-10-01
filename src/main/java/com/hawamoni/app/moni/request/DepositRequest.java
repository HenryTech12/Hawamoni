package com.hawamoni.app.moni.request;

public record DepositRequest(
        double amount,String recipientPubkey
) {
}
