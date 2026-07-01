package com.hms.service;

import com.hms.dto.MedicalRecordRequest;
import com.hms.dto.MedicalRecordResponse;
import com.hms.entity.Doctor;
import com.hms.entity.MedicalRecord;
import com.hms.entity.Patient;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.DoctorRepository;
import com.hms.repository.MedicalRecordRepository;
import com.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public MedicalRecordResponse createRecord(MedicalRecordRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setDiagnosis(request.getDiagnosis());
        record.setPrescription(request.getPrescription());
        record.setNotes(request.getNotes());

        MedicalRecord saved = medicalRecordRepository.save(record);
        return mapToResponse(saved);
    }

    public List<MedicalRecordResponse> getRecordsByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        return medicalRecordRepository.findByPatientId(patientId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private MedicalRecordResponse mapToResponse(MedicalRecord record) {
        MedicalRecordResponse response = new MedicalRecordResponse();
        response.setId(record.getId());
        response.setPatientId(record.getPatient().getId());
        response.setPatientName(record.getPatient().getName());
        response.setDoctorId(record.getDoctor().getId());
        response.setDoctorName(record.getDoctor().getName());
        response.setDiagnosis(record.getDiagnosis());
        response.setPrescription(record.getPrescription());
        response.setNotes(record.getNotes());
        return response;
    }
}
