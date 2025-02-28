package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PatientAppointmentDTO {
    private Long appointmentId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String doctorFullname;
    private String doctorSpecialty;
}
