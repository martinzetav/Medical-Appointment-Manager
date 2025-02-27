package com.app.service.impl;

import com.app.exception.ResourceNotFoundException;
import com.app.model.Appointment;
import com.app.repository.IAppointmentRepository;
import com.app.service.interfaces.IAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService {

    private final IAppointmentRepository appointmentRepository;

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> findById(Long id) throws ResourceNotFoundException {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if(appointment.isPresent()){
            return appointment;
        } else {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
    }

    @Override
    public Appointment update(Long id, Appointment appointment) throws ResourceNotFoundException {
        Optional<Appointment> appointmentWanted = appointmentRepository.findById(id);
        if(appointmentWanted.isPresent()){
            Appointment updatedAppointment = appointmentWanted.get();
            updatedAppointment.setPatient(appointment.getPatient());
            updatedAppointment.setDoctor(appointment.getDoctor());
            updatedAppointment.setStartDate(appointment.getStartDate());
            updatedAppointment.setEndDate(appointment.getEndDate());

            return this.save(updatedAppointment);
        } else {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Appointment appointment = this.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Appointment with id " + id + " not found"));

        appointmentRepository.deleteById(appointment.getId());
    }
}
