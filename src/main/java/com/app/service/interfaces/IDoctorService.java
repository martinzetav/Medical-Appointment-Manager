package com.app.service.interfaces;

import com.app.exception.ResourceNotFoundException;
import com.app.model.Doctor;

import java.util.List;
import java.util.Optional;

public interface IDoctorService {
    Doctor save(Doctor doctor);
    List<Doctor> findAll();
    Optional<Doctor> findById(Long id) throws ResourceNotFoundException;
    Doctor update(Long id, Doctor doctor) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
}
