package com.pm.service;


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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
   private PatientRepository patientRepository;
  private BillingClient billingClient;
 private KafkaProducer producer;
    public PatientService(PatientRepository patientRepository,
                          BillingClient billingClient,
                          KafkaProducer producer
    ) {
        this.patientRepository = patientRepository;
        this.billingClient = billingClient;
        this.producer=producer;
    }

   public List<PatientResponseDTO> getPatients(){
       List<Patient> patients = patientRepository.findAll();
       return patients.stream().map(PatientMapper::toDTO).toList();
   }

   public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
      if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
          throw new EmailAlreadyExistsException("A patient with ths email"+ "already exists"+ patientRequestDTO.getEmail());
      }

       Patient newPatient= patientRepository.save(PatientMapper.toModel(patientRequestDTO));
          producer.sendEvent(newPatient);
       BillingRequest billingRequest=new BillingRequest();
       billingRequest.setId(newPatient.getId());
       billingRequest.setName(newPatient.getName());
       billingRequest.setEmail(newPatient.getEmail());

       BillingResponse billingResponse=billingClient.createBilling(billingRequest);

   PatientResponseDTO response =PatientMapper.toDTO(newPatient);
   response.setBillingAccountId(billingResponse.getAccountId());
   response.setBillingStatus(billingResponse.getStatus());
   return response;
}
public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){
       Patient patient= patientRepository.findById(id).orElseThrow(()-> new PatientNotFoundException("Patient already exists"+id));

    if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)){
        throw new EmailAlreadyExistsException("A patient with ths email"+ "already exists"+ patientRequestDTO.getEmail());
    }
    patient.setName(patientRequestDTO.getName());
    patient.setAddress(patientRequestDTO.getAddress());
    patient.setEmail(patientRequestDTO.getEmail());
    patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

    Patient updatedPatient=patientRepository.save(patient);
    return PatientMapper.toDTO(updatedPatient);
}
public void deletePatient(UUID id){
       patientRepository.deleteById(id);
}

    public PatientResponseDTO getPatientById(UUID id) {

      Patient patient= patientRepository.getPatientById(id);
      return PatientMapper.toDTO(patient);
    }
}