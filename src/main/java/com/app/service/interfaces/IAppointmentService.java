package com.app.service.interfaces;

import com.app.exception.ResourceNotFoundException;
import com.app.model.Appointment;

import java.util.List;
import java.util.Optional;

public interface IAppointmentService {
    Appointment save(Appointment appointment);
    List<Appointment> findAll();
    Optional<Appointment> findById(Long id) throws ResourceNotFoundException;
    Appointment update(Long id, Appointment appointment) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
}
