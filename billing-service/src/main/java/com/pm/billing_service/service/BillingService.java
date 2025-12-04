package com.pm.billing_service.service;

import com.pm.billing_service.dto.BillingRequest;
import com.pm.billing_service.dto.BillingResponse;
import com.pm.billing_service.dto.PatientResponse;
import com.pm.billing_service.feign.PatientClient;
import org.springframework.stereotype.Service;

@Service
public class BillingService {


    private final PatientClient patientClient;
    public BillingService(PatientClient patientClient) {
        this.patientClient = patientClient;
    }
    public BillingResponse createBillingAccount(BillingRequest request){
        PatientResponse patient = patientClient.getPatientById(request.getId());
        BillingResponse response=new BillingResponse();
        response.setAccountId(("12345"));
        response.setStatus("Active");
        response.setName(patient.getName());
        response.setAmount(500.0);
        return response;
    }
}
