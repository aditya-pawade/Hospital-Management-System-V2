package com.hms.controller;

import com.hms.dto.ApiResponse;
import com.hms.dto.AppointmentRequest;
import com.hms.dto.AppointmentResponse;
import com.hms.service.AppointmentService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse appointment = appointmentService.createAppointment(request);
        ApiResponse<AppointmentResponse> response = new ApiResponse<>(201, "Appointment booked successfully", appointment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments();
        ApiResponse<List<AppointmentResponse>> response = new ApiResponse<>(200, "Appointments retrieved successfully", appointments);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(id);
        ApiResponse<AppointmentResponse> response = new ApiResponse<>(200, "Appointment retrieved successfully", appointment);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointment(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        AppointmentResponse appointment = appointmentService.updateAppointment(id, status);
        ApiResponse<AppointmentResponse> response = new ApiResponse<>(200, "Appointment updated successfully", appointment);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        ApiResponse<Object> response = new ApiResponse<>(200, "Appointment deleted successfully");
        return ResponseEntity.ok(response);
    }
}
