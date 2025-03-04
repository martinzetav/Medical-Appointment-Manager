package com.app.controller;

import com.app.dto.DoctorAppointmentDTO;
import com.app.dto.DoctorDTO;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Doctor;
import com.app.service.interfaces.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final IDoctorService doctorService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> findAll(){
        return ResponseEntity.ok(doctorService.findAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<DoctorDTO> requiredDoctor = doctorService.findById(id);
        ResponseEntity response;
        if(requiredDoctor.isPresent()){
            response = ResponseEntity.ok(requiredDoctor.get());
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @PostMapping
    public ResponseEntity<DoctorDTO> save(@RequestBody Doctor doctor){
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
    public ResponseEntity<Set<DoctorAppointmentDTO>> findAppointmentsByDoctorId(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(doctorService.findAppointmentsByDoctorId(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @GetMapping("/search")
    public ResponseEntity<List<DoctorDTO>> findDoctorBySpecialty(@RequestParam String specialty){
        return ResponseEntity.ok(doctorService.findDoctorBySpecialty(specialty));
    }
}
