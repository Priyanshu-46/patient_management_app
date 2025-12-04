package com.pm.feign;

import com.pm.dto.BillingRequest;
import com.pm.dto.BillingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(
        name = "billing-service",
        url = "http://localhost:8081"
)
public interface BillingClient {
    @PostMapping("/billing/create")
    BillingResponse createBilling(@RequestBody BillingRequest request);
}
