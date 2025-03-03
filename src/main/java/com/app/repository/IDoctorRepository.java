package com.app.repository;

import com.app.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findDoctorBySpecialty(String specialty);
}
