package com.hms.controller;

import com.hms.dto.ApiResponse;
import com.hms.dto.DoctorRequest;
import com.hms.dto.DoctorResponse;
import com.hms.service.DoctorService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@Valid @RequestBody DoctorRequest request) {
        DoctorResponse doctor = doctorService.createDoctor(request);
        ApiResponse<DoctorResponse> response = new ApiResponse<>(201, "Doctor created successfully", doctor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors() {
        List<DoctorResponse> doctors = doctorService.getAllDoctors();
        ApiResponse<List<DoctorResponse>> response = new ApiResponse<>(200, "Doctors retrieved successfully", doctors);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable Long id) {
        DoctorResponse doctor = doctorService.getDoctorById(id);
        ApiResponse<DoctorResponse> response = new ApiResponse<>(200, "Doctor retrieved successfully", doctor);
        return ResponseEntity.ok(response);
    }
}
