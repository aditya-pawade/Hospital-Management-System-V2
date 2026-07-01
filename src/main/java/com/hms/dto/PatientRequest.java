package com.hms.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PatientRequest {

    @NotBlank(message = "Patient name is required")
    private String name;

    @Min(value = 0, message = "Age must be a positive number")
    private int age;

    @NotBlank(message = "Gender is required")
    private String gender;

    private String contact;

    private String address;
}
