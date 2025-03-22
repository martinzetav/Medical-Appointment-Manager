package com.app.service.interfaces;

import com.app.dto.AppointmentDTO;
import com.app.exception.DuplicateResourceException;
import com.app.exception.InvalidDateException;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentService {
    AppointmentDTO save(Appointment appointment) throws ResourceNotFoundException, DuplicateResourceException;
    List<AppointmentDTO> findAll();
    Optional<AppointmentDTO> findById(Long id) throws ResourceNotFoundException;
    AppointmentDTO update(Long id, Appointment appointment) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
    List<AppointmentDTO> findAppointmentsForDay(int year, int month, int day) throws InvalidDateException;
}
