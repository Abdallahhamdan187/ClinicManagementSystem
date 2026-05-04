package com.example.clinicmanagementsystem.controller;

import com.example.clinicmanagementsystem.Service.DoctorService;
import com.example.clinicmanagementsystem.Service.MedialRecordService;
import com.example.clinicmanagementsystem.Service.PrescriptionService;
import com.example.clinicmanagementsystem.dto.DoctorDto;
import com.example.clinicmanagementsystem.dto.MedicalRecordDto;
import com.example.clinicmanagementsystem.entities.Appointment;
import com.example.clinicmanagementsystem.entities.Doctor;
import com.example.clinicmanagementsystem.entities.Invoice;
import com.example.clinicmanagementsystem.entities.MedicalRecord;
import com.example.clinicmanagementsystem.repo.PrescriptionRepo;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1/doctors")
@RestController
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;
    private final PrescriptionRepo prescriptionRepo;
    private final MedialRecordService medicalRecordService;


//    @PostMapping
//    public DoctorDto addDoctor(@Valid @RequestBody DoctorDto dto) {
//        return doctorService.addDoctor(dto);
//    }

    @GetMapping
    public Page<Doctor> getDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @Nullable String specialization,
            @RequestParam(required = false) @Nullable String name) {
        return doctorService.getDoctors(page, size, specialization, name);
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable long id) {
        return doctorService.getDoctorById(id);
    }

    @PutMapping("/{id}")
    public DoctorDto updateDoctor(@PathVariable long id, @RequestBody DoctorDto dto) {
        return doctorService.updateDoctor(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable long id) {
        doctorService.deleteDoctor(id);
    }

    @GetMapping("/{id}/schedule")
    public List<Appointment> getSchedule(@PathVariable Long id) {
        return doctorService.getSchedule(id);
    }

    @GetMapping("/{id}/total-earnings")
    public Integer getTotalEarning(@PathVariable long id) {
        return doctorService.getTotalEarning(id);
    }

    @GetMapping("/{id}/today-earnings")
    public Double getTodayEarning(@PathVariable long id) {
        return doctorService.getTodayEarning(id);
    }

    @GetMapping("/{id}/range-earnings")
    public Double getEarningFromRange(
            @PathVariable long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return doctorService.getEarningFromRange(id, start, end);
    }
    @GetMapping("/{id}/appointments")
    public List<Appointment> getAppointmentsFromRange(
            @PathVariable long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return doctorService.getAppointmentsFromRange(id, start, end);
    }

    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices() {
        return doctorService.getInvoices();
    }

    @PutMapping("/{id}/expire")
    public void expire(@PathVariable long id) {
        prescriptionService.expire(id);


    }


    @PostMapping("/addMedical")
    public MedicalRecordDto addMedicalRecord(@RequestBody MedicalRecordDto dto) {
        return medicalRecordService.addMedicalRecord(dto);
    }

    @GetMapping("/medicalRecord")
    public Page<MedicalRecord> getMedicalRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @Nullable String diagnosis,
            @RequestParam(required = false) @Nullable Long patientId) {
        return medicalRecordService.findWithFilters(page, size, diagnosis, patientId);
    }

    @GetMapping("/{id}/medical")
    public MedicalRecord getById(@PathVariable long id) {
        return medicalRecordService.getMedicalRecordById(id);
    }
    @PreAuthorize("hasRole('Doctor')")

    @PutMapping("/{id}/medical")
    public MedicalRecordDto updateMedicalRecord(
            @PathVariable long id,
            @RequestBody MedicalRecordDto dto) {
        return medicalRecordService.updateMedicalRecord(id, dto);
    }

    @DeleteMapping("/{id}/medical")
    public void deleteMedicalRecord(@PathVariable long id) {
        medicalRecordService.deleteMedicalRecord(id);
    }
}
