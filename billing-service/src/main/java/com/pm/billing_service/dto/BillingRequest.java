package com.pm.billing_service.dto;

import java.util.UUID;

public class BillingRequest {

    private UUID id;

    public BillingRequest() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
