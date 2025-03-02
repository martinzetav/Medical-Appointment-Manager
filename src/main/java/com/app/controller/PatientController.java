package com.app.controller;

import com.app.dto.PatientAppointmentDTO;
import com.app.dto.PatientDTO;
import com.app.exception.DuplicateResourceException;
import com.app.exception.ResourceNotFoundException;
import com.app.model.Patient;
import com.app.service.interfaces.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> findAll(){
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<PatientDTO> requiredPatient = patientService.findById(id);
        ResponseEntity<PatientDTO> response;
        if(requiredPatient.isPresent()){
            response = ResponseEntity.ok(requiredPatient.get());
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @PostMapping
    public ResponseEntity<PatientDTO> save(@RequestBody Patient patient) throws DuplicateResourceException {
        return ResponseEntity.ok(patientService.save(patient));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws ResourceNotFoundException {
        patientService.delete(id);
        return ResponseEntity.ok("Patient with id " + id + " successfully removed.");
    }

    @GetMapping("/{id}/appointments")
    public ResponseEntity<Set<PatientAppointmentDTO>> findAppointmentsByPatientId(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(patientService.findAppointmentsByPatientId(id));
    }

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
