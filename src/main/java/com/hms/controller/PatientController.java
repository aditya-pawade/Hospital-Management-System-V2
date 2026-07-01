package com.hms.controller;

import com.hms.dto.ApiResponse;
import com.hms.dto.PatientRequest;
import com.hms.dto.PatientResponse;
import com.hms.service.PatientService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(@Valid @RequestBody PatientRequest request) {
        PatientResponse patient = patientService.createPatient(request);
        ApiResponse<PatientResponse> response = new ApiResponse<>(201, "Patient created successfully", patient);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatients() {
        List<PatientResponse> patients = patientService.getAllPatients();
        ApiResponse<List<PatientResponse>> response = new ApiResponse<>(200, "Patients retrieved successfully", patients);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable Long id) {
        PatientResponse patient = patientService.getPatientById(id);
        ApiResponse<PatientResponse> response = new ApiResponse<>(200, "Patient retrieved successfully", patient);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        PatientResponse patient = patientService.updatePatient(id, request);
        ApiResponse<PatientResponse> response = new ApiResponse<>(200, "Patient updated successfully", patient);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        ApiResponse<Object> response = new ApiResponse<>(200, "Patient deleted successfully");
        return ResponseEntity.ok(response);
    }
}
