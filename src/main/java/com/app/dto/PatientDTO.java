package com.app.dto;

import com.app.model.Address;
import com.app.model.Contact;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class PatientDTO {
    private Long id;
    private String name;
    private String lastName;
    private String dni;
    private Contact contact;
    private Address address;
    private Set<PatientAppointmentDTO> appointments;
}
