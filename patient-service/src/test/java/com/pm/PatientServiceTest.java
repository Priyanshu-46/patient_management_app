package com.pm;


import com.pm.dto.BillingRequest;
import com.pm.dto.BillingResponse;
import com.pm.dto.PatientRequestDTO;
import com.pm.dto.PatientResponseDTO;
import com.pm.exception.EmailAlreadyExistsException;
import com.pm.exception.PatientNotFoundException;
import com.pm.feign.BillingClient;
import com.pm.kafka.KafkaProducer;
import com.pm.mapper.PatientMapper;
import com.pm.model.Patient;
import com.pm.repository.PatientRepository;
import com.pm.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

 @Mock
 private PatientRepository patientRepository;

 @Mock
 private BillingClient billingClient;

 @Mock
 private KafkaProducer producer;

 @InjectMocks
  private PatientService patientService;

    @Test
    void testCreatePatient() {

        PatientRequestDTO request = new PatientRequestDTO();
        request.setName("John");
        request.setEmail("john@gmail.com");
        request.setAddress("123");
        request.setDateOfBirth("1990-10-10");
        request.setRegisteredDate("2025-01-01");

        when(patientRepository.existsByEmail("john@gmail.com")).thenReturn(false);

        Patient saved = new Patient();
        saved.setId(UUID.randomUUID());
        saved.setName("John");
        saved.setEmail("john@gmail.com");
        saved.setDateOfBirth(LocalDate.parse("1990-10-10"));
        saved.setRegisteredDate(LocalDate.parse("2025-01-01"));

        when(patientRepository.save(any(Patient.class))).thenReturn(saved);

        BillingResponse billingResponse = new BillingResponse();
        billingResponse.setAccountId("ACC123");
        billingResponse.setStatus("CREATED");

        when(billingClient.createBilling(any(BillingRequest.class)))
                .thenReturn(billingResponse);

        PatientResponseDTO response = patientService.createPatient(request);

        assertEquals("ACC123", response.getBillingAccountId());
        assertEquals("CREATED", response.getBillingStatus());

        verify(producer, times(1)).sendEvent(any(Patient.class));
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

}
