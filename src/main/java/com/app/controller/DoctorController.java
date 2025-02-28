package com.app.controller;

import com.app.exception.ResourceNotFoundException;
import com.app.model.Doctor;
import com.app.service.interfaces.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final IDoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<Doctor>> findAll(){
        return ResponseEntity.ok(doctorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Doctor> requiredDoctor = doctorService.findById(id);
        ResponseEntity response;
        if(requiredDoctor.isPresent()){
            response = ResponseEntity.ok(requiredDoctor.get());
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @PostMapping
    public ResponseEntity<Doctor> save(@RequestBody Doctor doctor){
        return ResponseEntity.ok(doctorService.save(doctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> update(@PathVariable Long id, @RequestBody Doctor doctor) throws ResourceNotFoundException {
        Optional<Doctor> requiredDoctor = doctorService.findById(id);
        ResponseEntity response;
        if(requiredDoctor.isPresent()){
            response = ResponseEntity.ok(doctorService.update(id, doctor));
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws ResourceNotFoundException {
        doctorService.delete(id);
        return ResponseEntity.ok("Doctor with id " + id + " successfully removed.");
    }

}
