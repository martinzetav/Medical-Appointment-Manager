package com.app.service.impl;

import com.app.exception.ResourceNotFoundException;
import com.app.model.Doctor;
import com.app.repository.IDoctorRepository;
import com.app.service.interfaces.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService implements IDoctorService {

    private final IDoctorRepository doctorRepository;

    @Override
    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> findById(Long id) throws ResourceNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if(doctor.isPresent()){
            return doctor;
        } else {
            throw new ResourceNotFoundException("Doctor with id " + id + " not found");
        }
    }

    @Override
    public Doctor update(Long id, Doctor doctor) throws ResourceNotFoundException {
        Optional<Doctor> doctorWanted = doctorRepository.findById(id);
        if(doctorWanted.isPresent()){
            Doctor updatedDoctor = doctorWanted.get();
            updatedDoctor.setName(doctor.getName());
            updatedDoctor.setLastName(doctor.getLastName());
            updatedDoctor.setSpecialty(doctor.getSpecialty());
            updatedDoctor.setContact(doctor.getContact());
            updatedDoctor.setAppointments(doctor.getAppointments());

            return this.save(updatedDoctor);
        } else {
            throw new ResourceNotFoundException("Doctor with id " + id + " not found");
        }
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Doctor doctor = this.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Doctor with id " + id + " not found"));

        doctorRepository.deleteById(doctor.getId());
    }
}
