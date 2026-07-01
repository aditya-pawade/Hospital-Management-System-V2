package com.hms.controller;

import com.hms.dto.ApiResponse;
import com.hms.dto.MedicalRecordRequest;
import com.hms.dto.MedicalRecordResponse;
import com.hms.service.MedicalRecordService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> createRecord(@Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecordResponse record = medicalRecordService.createRecord(request);
        ApiResponse<MedicalRecordResponse> response = new ApiResponse<>(201, "Medical record created successfully", record);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/patient/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<List<MedicalRecordResponse>>> getRecordsByPatientId(@PathVariable Long id) {
        List<MedicalRecordResponse> records = medicalRecordService.getRecordsByPatientId(id);
        ApiResponse<List<MedicalRecordResponse>> response = new ApiResponse<>(200, "Medical records retrieved successfully", records);
        return ResponseEntity.ok(response);
    }
}
