package com.app.controller;

import com.app.dto.DoctorAppointmentDTO;
import com.app.dto.DoctorDTO;
import com.app.exception.DuplicateResourceException;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Doctor;
import com.app.service.interfaces.IDoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Controller for Doctors")
public class DoctorController {

    private final IDoctorService doctorService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> findAll(){
        return ResponseEntity.ok(doctorService.findAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY', 'ROLE_DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        boolean isAdminOrSecretary = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_SECRETARY"));

        Optional<DoctorDTO> requiredDoctor = doctorService.findById(id);

        if(isAdminOrSecretary){
            return ResponseEntity.ok(requiredDoctor.get());
        }

        if (requiredDoctor.isEmpty() || !requiredDoctor.get().getDni().equals(currentUsername)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(requiredDoctor.get());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @PostMapping
    public ResponseEntity<DoctorDTO> save(@RequestBody Doctor doctor) throws ResourceNotFoundException, DuplicateResourceException {
        return ResponseEntity.ok(doctorService.save(doctor));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> update(@PathVariable Long id, @RequestBody Doctor doctor) throws ResourceNotFoundException {
        Optional<DoctorDTO> requiredDoctor = doctorService.findById(id);
        ResponseEntity response;
        if(requiredDoctor.isPresent()){
            response = ResponseEntity.ok(doctorService.update(id, doctor));
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws ResourceNotFoundException {
        doctorService.delete(id);
        return ResponseEntity.ok("Doctor with id " + id + " successfully removed.");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY', 'ROLE_DOCTOR')")
    @GetMapping("/{id}/appointments")
    @Operation(summary = "Retrieve all appointments for a specific doctor by their ID.")
    public ResponseEntity<Set<DoctorAppointmentDTO>> findAppointmentsByDoctorId(@PathVariable Long id) throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        boolean isAdminOrSecretary = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_SECRETARY"));
        Optional<DoctorDTO> requiredDoctor = doctorService.findById(id);

        if(isAdminOrSecretary){
            return ResponseEntity.ok(doctorService.findAppointmentsByDoctorId(id));
        }

        if (requiredDoctor.isEmpty() || !requiredDoctor.get().getDni().equals(currentUsername)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctorService.findAppointmentsByDoctorId(id));

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @GetMapping("/search")
    @Operation(summary = "Retrieve all doctors based on their specialty.")
    public ResponseEntity<List<DoctorDTO>> findDoctorBySpecialty(@RequestParam String specialty){
        return ResponseEntity.ok(doctorService.findDoctorBySpecialty(specialty));
    }
}
