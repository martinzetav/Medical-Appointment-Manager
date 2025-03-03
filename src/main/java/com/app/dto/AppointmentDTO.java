package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AppointmentDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String patientName;
    private String patientLastName;
    private String doctorName;
    private String doctorLastName;
}
