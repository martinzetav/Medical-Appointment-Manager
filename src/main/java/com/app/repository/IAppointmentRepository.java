package com.app.repository;

import com.app.model.Appointment;
import com.app.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface IAppointmentRepository extends JpaRepository<Appointment, Long> {
    Set<Appointment> findByPatientId(Long patientId);
    Set<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByDoctorAndStartDateBetween(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);
}
