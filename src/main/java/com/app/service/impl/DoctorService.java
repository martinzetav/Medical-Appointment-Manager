package com.app.service.impl;

import com.app.dto.DoctorAppointmentDTO;
import com.app.dto.DoctorDTO;
import com.app.exception.DuplicateResourceException;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Appointment;
import com.app.model.Doctor;
import com.app.model.UserEntity;
import com.app.repository.IAppointmentRepository;
import com.app.repository.IDoctorRepository;
import com.app.repository.IUserRepository;
import com.app.service.interfaces.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService implements IDoctorService {

    private final IDoctorRepository doctorRepository;
    private final IAppointmentRepository appointmentRepository;
    private final IUserRepository userRepository;

    @Override
    public DoctorDTO save(Doctor doctor) throws DuplicateResourceException, ResourceNotFoundException {
        if(doctorRepository.existsByDni(doctor.getDni())){
            throw new DuplicateResourceException("A Doctor with this DNI already exists.");
        }

        UserEntity user = userRepository.findUserEntityByUsername(doctor.getDni())
                .orElseThrow(() -> new ResourceNotFoundException("No registered user found with the provided DNI."));

        doctorRepository.save(doctor);
        return this.convertToDoctorDTO(doctor);
    }

    @Override
    public List<DoctorDTO> findAll() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(this::convertToDoctorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DoctorDTO> findById(Long id) throws ResourceNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if(doctor.isPresent()){
            DoctorDTO doctorDTO = this.convertToDoctorDTO(doctor.get());
            return Optional.of(doctorDTO);
        } else {
            throw new ResourceNotFoundException("Doctor with id " + id + " not found");
        }
    }

    @Override
    public DoctorDTO update(Long id, Doctor doctor) throws ResourceNotFoundException {
        Optional<Doctor> doctorWanted = doctorRepository.findById(id);
        if(doctorWanted.isPresent()){
            Doctor updatedDoctor = doctorWanted.get();
            updatedDoctor.setName(doctor.getName());
            updatedDoctor.setLastName(doctor.getLastName());
            updatedDoctor.setSpecialty(doctor.getSpecialty());
            updatedDoctor.setContact(doctor.getContact());
            updatedDoctor.setAppointments(doctor.getAppointments());

            doctorRepository.save(updatedDoctor);

            DoctorDTO doctorDTO = this.convertToDoctorDTO(updatedDoctor);

            return doctorDTO;
        } else {
            throw new ResourceNotFoundException("Doctor with id " + id + " not found");
        }
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Doctor with id " + id + " not found"));

        doctorRepository.deleteById(doctor.getId());
    }

    @Override
    public Set<DoctorAppointmentDTO> findAppointmentsByDoctorId(Long id) throws ResourceNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if(doctor.isPresent()){
            Set<Appointment> appointments = appointmentRepository.findByDoctorId(id);
                return appointments.stream()
                        .map(appointment -> DoctorAppointmentDTO.builder()
                                .appointmentId(appointment.getId())
                                .startDate(appointment.getStartDate())
                                .endDate(appointment.getEndDate())
                                .patientName(appointment.getPatient().getName())
                                .patientLastName(appointment.getPatient().getLastName())
                                .patientDni(appointment.getPatient().getDni())
                                .patientEmail(appointment.getPatient().getContact().getEmail())
                                .build()
                        )
                        .collect(Collectors.toSet());

        } else {
            throw new ResourceNotFoundException("Doctor with id " + id + " not found");
        }
    }

    @Override
    public List<DoctorDTO> findDoctorBySpecialty(String specialty) {
        List<Doctor> doctors = doctorRepository.findDoctorBySpecialty(specialty);
        return doctors.stream()
                .map(this::convertToDoctorDTO)
                .collect(Collectors.toList());
    }

    private DoctorDTO convertToDoctorDTO(Doctor doctor){
        return DoctorDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .lastName(doctor.getLastName())
                .dni(doctor.getDni())
                .specialty(doctor.getSpecialty())
                .contact(doctor.getContact())
                .appointments(doctor.getAppointments().stream()
                        .map(appointment -> DoctorAppointmentDTO.builder()
                                .appointmentId(appointment.getId())
                                .startDate(appointment.getStartDate())
                                .endDate(appointment.getEndDate())
                                .patientName(appointment.getPatient().getName())
                                .patientLastName(appointment.getPatient().getLastName())
                                .patientEmail(appointment.getPatient().getContact().getEmail())
                                .build())
                        .collect(Collectors.toSet()))
                .build();

    }
}
