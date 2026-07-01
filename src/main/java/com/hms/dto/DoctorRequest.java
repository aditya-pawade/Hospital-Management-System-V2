package com.hms.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DoctorRequest {

    @NotBlank(message = "Doctor name is required")
    private String name;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    private String contact;
}
