package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DoctorAppointmentDTO {
    private Long appointmentId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String patientFullName;
    private String patientEmail;
}
