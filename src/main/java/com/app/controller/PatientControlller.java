package com.app.controller;

import com.app.exception.ResourceNotFoundException;
import com.app.model.Patient;
import com.app.service.interfaces.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientControlller {

    private final IPatientService patientService;

    @GetMapping
    public ResponseEntity<List<Patient>> findAll(){
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Patient> requiredPatient = patientService.findById(id);
        ResponseEntity<Patient> response;
        if(requiredPatient.isPresent()){
            response = ResponseEntity.ok(requiredPatient.get());
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @PostMapping
    public ResponseEntity<Patient> save(@RequestBody Patient patient){
        return ResponseEntity.ok(patientService.save(patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(@PathVariable Long id, @RequestBody Patient patient) throws ResourceNotFoundException {
        Optional<Patient> patientToLookFor = patientService.findById(id);
        ResponseEntity<Patient> response;
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

}
