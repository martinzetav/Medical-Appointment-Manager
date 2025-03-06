package com.app.dto;

import com.app.model.Contact;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class DoctorDTO {
    private Long id;
    private String name;
    private String lastName;
    private String dni;
    private String specialty;
    private Contact contact;
    private Set<DoctorAppointmentDTO> appointments;
}
