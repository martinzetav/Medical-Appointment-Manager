package com.app.controller;

import com.app.dto.AppointmentDTO;
import com.app.exception.DuplicateResourceException;
import com.app.exception.InvalidDateException;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Appointment;
import com.app.service.interfaces.IAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Controller for Appointments")
public class AppointmentController {

    private final IAppointmentService appointmentService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> findAll(){
        return ResponseEntity.ok(appointmentService.findAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<AppointmentDTO> appointment = appointmentService.findById(id);
        ResponseEntity response;
        if(appointment.isPresent()){
            response = ResponseEntity.ok(appointment.get());
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @PostMapping
    public ResponseEntity<AppointmentDTO> save(@RequestBody Appointment appointment) throws ResourceNotFoundException, DuplicateResourceException {
        return ResponseEntity.ok(appointmentService.save(appointment));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> update(@PathVariable Long id, @RequestBody Appointment appointment) throws ResourceNotFoundException {
        Optional<AppointmentDTO> requiredAppointment = appointmentService.findById(id);
        ResponseEntity response;
        if(requiredAppointment.isPresent()){
            response = ResponseEntity.ok(appointmentService.update(id, appointment));
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws ResourceNotFoundException {
        appointmentService.delete(id);
        return ResponseEntity.ok("Appointment with id " + id + " successfully removed.");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @GetMapping("/day")
    @Operation(summary = "Fetch all appointments for specific day")
    public ResponseEntity<List<AppointmentDTO>> findAppointmentsForDay(@RequestParam int year,
                                                                       @RequestParam int month,
                                                                       @RequestParam int day) throws InvalidDateException {
        return ResponseEntity.ok(appointmentService.findAppointmentsForDay(year, month, day));
    }
}
