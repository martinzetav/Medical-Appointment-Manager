package com.app.service.impl;

import com.app.dto.DoctorAppointmentDTO;
import com.app.dto.PatientAppointmentDTO;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Appointment;
import com.app.model.Patient;
import com.app.repository.IAppointmentRepository;
import com.app.repository.IPatientRepository;
import com.app.service.interfaces.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService implements IPatientService {

    private final IPatientRepository patientRepository;
    private final IAppointmentRepository appointmentRepository;

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findById(Long id) throws ResourceNotFoundException {
        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isPresent()){
            return patient;
        } else {
            throw new ResourceNotFoundException("Patient with id " + id + " not found");
        }
    }

    @Override
    public Patient update(Long id, Patient patient) throws ResourceNotFoundException {
        Optional<Patient> patientWanted = patientRepository.findById(id);
        if(patientWanted.isPresent()){
            Patient updatedPatient = patientWanted.get();
            updatedPatient.setName(patient.getName());
            updatedPatient.setLastName(patient.getLastName());
            updatedPatient.setContact(patient.getContact());
            updatedPatient.setAddress(patient.getAddress());
            updatedPatient.setAppointments(patient.getAppointments());

            return this.save(updatedPatient);
        } else {
            throw new ResourceNotFoundException("Patient with id " + id + " not found");
        }
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Patient patient = this.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Patient with id " + id + " not found"));

        patientRepository.deleteById(patient.getId());
    }

    @Override
    public Set<PatientAppointmentDTO> findAppointmentsByPatientId(Long id) throws ResourceNotFoundException {
        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isPresent()){
            Set<Appointment> appointments = appointmentRepository.findByPatientId(id);
            return appointments.stream()
                    .map(appointment -> PatientAppointmentDTO.builder()
                            .appointmentId(appointment.getId())
                            .startDate(appointment.getStartDate())
                            .endDate(appointment.getEndDate())
                            .doctorFullName(appointment.getDoctor().getName() + " " + appointment.getDoctor().getLastName())
                            .doctorSpecialty(appointment.getDoctor().getSpecialty())
                            .build()
                    )
                    .collect(Collectors.toSet());
        } else {
            throw new ResourceNotFoundException("Patient with id " + id + " not found");
        }
    }
}
