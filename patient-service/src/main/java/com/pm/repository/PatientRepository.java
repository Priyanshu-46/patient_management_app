package com.pm.repository;

import com.pm.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
boolean existsByEmail(String email);
boolean existsByEmailAndIdNot(String email,UUID id);

    Patient getPatientById(UUID id);
}