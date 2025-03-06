package com.app.service.interfaces;

import com.app.dto.DoctorAppointmentDTO;
import com.app.dto.DoctorDTO;
import com.app.exception.DuplicateResourceException;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Doctor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IDoctorService {
    DoctorDTO save(Doctor doctor) throws DuplicateResourceException, ResourceNotFoundException;
    List<DoctorDTO> findAll();
    Optional<DoctorDTO> findById(Long id) throws ResourceNotFoundException;
    DoctorDTO update(Long id, Doctor doctor) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
    Set<DoctorAppointmentDTO> findAppointmentsByDoctorId(Long id) throws ResourceNotFoundException;
    List<DoctorDTO> findDoctorBySpecialty(String specialty);
}
