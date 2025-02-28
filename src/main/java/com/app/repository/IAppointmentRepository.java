package com.app.repository;

import com.app.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IAppointmentRepository extends JpaRepository<Appointment, Long> {
    Set<Appointment> findByPatientId(Long patientId);
    Set<Appointment> findByDoctorId(Long doctorId);
}
