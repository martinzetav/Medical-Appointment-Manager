package com.app.controller;

import com.app.dto.PatientAppointmentDTO;
import com.app.dto.PatientDTO;
import com.app.exception.DuplicateResourceException;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Patient;
import com.app.service.interfaces.IPatientService;
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
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientService patientService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @GetMapping
    public ResponseEntity<List<PatientDTO>> findAll(){
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY', 'ROLE_USER')")
    public ResponseEntity<PatientDTO> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        boolean isAdminOrSecretary = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_SECRETARY"));

        Optional<PatientDTO> requiredPatient = patientService.findById(id);

        if(isAdminOrSecretary){
            return ResponseEntity.ok(requiredPatient.get());
        }

        if (requiredPatient.isEmpty() || !requiredPatient.get().getDni().equals(currentUsername)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(requiredPatient.get());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @PostMapping
    public ResponseEntity<PatientDTO> save(@RequestBody Patient patient) throws DuplicateResourceException, ResourceNotFoundException {
        return ResponseEntity.ok(patientService.save(patient));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> update(@PathVariable Long id, @RequestBody Patient patient) throws ResourceNotFoundException {
        Optional<PatientDTO> patientToLookFor = patientService.findById(id);
        ResponseEntity<PatientDTO> response;
        if(patientToLookFor.isPresent()){
            response = ResponseEntity.ok(patientService.update(id, patient));
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws ResourceNotFoundException {
        patientService.delete(id);
        return ResponseEntity.ok("Patient with id " + id + " successfully removed.");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY', 'ROLE_USER')")
    @GetMapping("/{id}/appointments")
    public ResponseEntity<Set<PatientAppointmentDTO>> findAppointmentsByPatientId(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(patientService.findAppointmentsByPatientId(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SECRETARY')")
    @GetMapping("/search")
    public ResponseEntity<PatientDTO> findPatientByDni(@RequestParam String dni) throws ResourceNotFoundException {
        Optional<PatientDTO> requiredPatient = patientService.findPatientByDni(dni);
        ResponseEntity<PatientDTO> response;
        if(requiredPatient.isPresent()){
            response = ResponseEntity.ok(requiredPatient.get());
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

}
