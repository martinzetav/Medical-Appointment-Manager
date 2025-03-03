package com.app.service.impl;

import com.app.dto.PatientAppointmentDTO;
import com.app.dto.PatientDTO;
import com.app.exception.DuplicateResourceException;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Appointment;
import com.app.model.Patient;
import com.app.repository.IAppointmentRepository;
import com.app.repository.IPatientRepository;
import com.app.service.interfaces.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public PatientDTO save(Patient patient) throws DuplicateResourceException {
        if(patientRepository.existsByDni(patient.getDni())){
            throw new DuplicateResourceException("A patient with this DNI already exists.");
        }
        patientRepository.save(patient);
        return this.convertToPatientDTO(patient);
    }

    @Override
    public List<PatientDTO> findAll() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(this::convertToPatientDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PatientDTO> findById(Long id) throws ResourceNotFoundException {
        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isPresent()){
            PatientDTO patientDTO = this.convertToPatientDTO(patient.get());
            return Optional.of(patientDTO);
        } else {
            throw new ResourceNotFoundException("Patient with id " + id + " not found");
        }
    }

    @Override
    public PatientDTO update(Long id, Patient patient) throws ResourceNotFoundException {
        Optional<Patient> patientWanted = patientRepository.findById(id);
        if(patientWanted.isPresent()){
            Patient updatedPatient = patientWanted.get();
            updatedPatient.setName(patient.getName());
            updatedPatient.setLastName(patient.getLastName());
            updatedPatient.setContact(patient.getContact());
            updatedPatient.setAddress(patient.getAddress());
            updatedPatient.setAppointments(patient.getAppointments());

            patientRepository.save(updatedPatient);

            PatientDTO patientDTO = this.convertToPatientDTO(updatedPatient);

            return patientDTO;
        } else {
            throw new ResourceNotFoundException("Patient with id " + id + " not found");
        }
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Patient patient = patientRepository.findById(id)
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
                            .doctorName(appointment.getDoctor().getName())
                            .doctorLastName(appointment.getDoctor().getLastName())
                            .doctorSpecialty(appointment.getDoctor().getSpecialty())
                            .build()
                    )
                    .collect(Collectors.toSet());
        } else {
            throw new ResourceNotFoundException("Patient with id " + id + " not found");
        }
    }

    @Override
    public Optional<PatientDTO> findPatientByDni(String dni) throws ResourceNotFoundException {
        Optional<Patient> patient = patientRepository.findPatientByDni(dni);
        if(patient.isPresent()){
            PatientDTO patientDTO = this.convertToPatientDTO(patient.get());
            return Optional.of((patientDTO));
        } else {
            throw new ResourceNotFoundException("Patient with DNI " + dni + " not found.");
        }
    }

    private PatientDTO convertToPatientDTO(Patient patient){
        return PatientDTO.builder()
                .id(patient.getId())
                .name(patient.getName())
                .lastName(patient.getLastName())
                .dni(patient.getDni())
                .contact(patient.getContact())
                .address(patient.getAddress())
                .appointments(patient.getAppointments().stream()
                        .map(appointment -> PatientAppointmentDTO.builder()
                                .appointmentId(appointment.getId())
                                .startDate(appointment.getStartDate())
                                .endDate(appointment.getEndDate())
                                .doctorName(appointment.getDoctor().getName())
                                .doctorLastName(appointment.getDoctor().getLastName())
                                .doctorSpecialty(appointment.getDoctor().getSpecialty())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
