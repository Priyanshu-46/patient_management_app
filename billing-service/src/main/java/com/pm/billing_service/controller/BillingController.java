package com.pm.billing_service.controller;

import com.pm.billing_service.dto.BillingRequest;
import com.pm.billing_service.dto.BillingResponse;
import com.pm.billing_service.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }
    @PostMapping("/create")
    public ResponseEntity<BillingResponse> createBilling(@RequestBody BillingRequest request){
        BillingResponse response= billingService.createBillingAccount(request);
        return ResponseEntity.ok(response);
    }


}