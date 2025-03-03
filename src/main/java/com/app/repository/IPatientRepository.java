package com.app.repository;

import com.app.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByDni(String dni);
    Optional<Patient> findPatientByDni(String dni);
}
