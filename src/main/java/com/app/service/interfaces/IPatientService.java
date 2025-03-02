package com.app.service.interfaces;

import com.app.dto.PatientAppointmentDTO;
import com.app.dto.PatientDTO;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Patient;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IPatientService {
    PatientDTO save(Patient patient);
    List<PatientDTO> findAll();
    Optional<PatientDTO> findById(Long id) throws ResourceNotFoundException;
    PatientDTO update(Long id, Patient patient) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
    Set<PatientAppointmentDTO> findAppointmentsByPatientId(Long id) throws ResourceNotFoundException;
}
