package com.app.service.impl;

import com.app.dto.AppointmentDTO;
import com.app.dto.DoctorDTO;
import com.app.dto.PatientDTO;
import com.app.exception.DuplicateResourceException;
import com.app.exception.InvalidDateException;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Appointment;
import com.app.model.Doctor;
import com.app.model.Patient;
import com.app.repository.IAppointmentRepository;
import com.app.service.interfaces.IAppointmentService;
import com.app.service.interfaces.IDoctorService;
import com.app.service.interfaces.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService {

    private final IAppointmentRepository appointmentRepository;
    private final IPatientService patientService;
    private final IDoctorService doctorService;

    @Override
    public AppointmentDTO save(Appointment appointment) throws ResourceNotFoundException, DuplicateResourceException {
        Optional<PatientDTO> patientDTO = patientService.findById(appointment.getPatient().getId());
        Optional<DoctorDTO> doctorDTO = doctorService.findById(appointment.getDoctor().getId());
        if(patientDTO.isPresent() && doctorDTO.isPresent()){
            Patient patient = new Patient();
            patient.setId(patientDTO.get().getId());
            patient.setName(patientDTO.get().getName());
            patient.setLastName(patientDTO.get().getLastName());

            Doctor doctor = new Doctor();
            doctor.setId(doctorDTO.get().getId());
            doctor.setName(doctorDTO.get().getName());
            doctor.setLastName(doctorDTO.get().getLastName());

            appointment.setEndDate(appointment.getStartDate().plusMinutes(30));

            List<Appointment> conflictingAppointments = appointmentRepository.findByDoctorAndStartDateBetween(
                    appointment.getDoctor(),
                    appointment.getStartDate(),
                    appointment.getEndDate()
            );
            if(!conflictingAppointments.isEmpty()){
                throw new DuplicateResourceException("There is already an appointment for this doctor at the given time.");
            }

            appointment.setPatient(patient);
            appointment.setDoctor(doctor);

            appointmentRepository.save(appointment);
            return this.convertToAppointmentDTO(appointment);
        } else {
            throw new ResourceNotFoundException("Patient or Doctor not found.");
        }
    }

    @Override
    public List<AppointmentDTO> findAll() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AppointmentDTO> findById(Long id) throws ResourceNotFoundException {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if(appointment.isPresent()){
            AppointmentDTO appointmentDTO = this.convertToAppointmentDTO(appointment.get());
            return Optional.of(appointmentDTO);
        } else {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
    }

    @Override
    public AppointmentDTO update(Long id, Appointment appointment) throws ResourceNotFoundException {
        Optional<Appointment> appointmentWanted = appointmentRepository.findById(id);
        if(appointmentWanted.isPresent()){
            Appointment updatedAppointment = appointmentWanted.get();
            updatedAppointment.setPatient(appointment.getPatient());
            updatedAppointment.setDoctor(appointment.getDoctor());
            updatedAppointment.setStartDate(appointment.getStartDate());
            updatedAppointment.setEndDate(appointment.getEndDate());

            appointmentRepository.save(updatedAppointment);

            AppointmentDTO appointmentDTO = this.convertToAppointmentDTO(updatedAppointment);
            return appointmentDTO;
        } else {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Appointment with id " + id + " not found"));

        appointmentRepository.deleteById(appointment.getId());
    }

    public List<AppointmentDTO> findAppointmentsForDay(int year, int month, int day) throws InvalidDateException {
        if(month < 1 || month > 12) throw new InvalidDateException("The month provided is not valid");
        if(day < 1 || day > YearMonth.of(year, month).lengthOfMonth()) throw new InvalidDateException("the day provided is not valid");
        LocalDateTime startOfDay = LocalDateTime.of(year, month, day, 0, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
        List<Appointment> appointments = appointmentRepository.findByStartDateBetween(startOfDay, endOfDay);
        return appointments.stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    private AppointmentDTO convertToAppointmentDTO(Appointment appointment){
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .startDate(appointment.getStartDate())
                .endDate(appointment.getEndDate())
                .patientName(appointment.getPatient().getName())
                .patientLastName(appointment.getPatient().getLastName())
                .doctorName(appointment.getDoctor().getName())
                .doctorLastName(appointment.getDoctor().getLastName())
                .build();
    }
}
