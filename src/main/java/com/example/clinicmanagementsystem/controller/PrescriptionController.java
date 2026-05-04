package com.example.clinicmanagementsystem.controller;

import com.example.clinicmanagementsystem.Service.PrescriptionService;
import com.example.clinicmanagementsystem.dto.PrescriptionDto;
import com.example.clinicmanagementsystem.dto.PrescriptionStatus;
import com.example.clinicmanagementsystem.entities.Prescription;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/presc")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public PrescriptionDto addPrescription(@RequestBody PrescriptionDto dto) {
        return prescriptionService.addPrescription(dto);
    }

    @GetMapping
    public Page<Prescription> getFilteredPrescriptions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @Nullable @RequestParam(required = false) PrescriptionStatus status, @RequestParam(required = false) long pId, @RequestParam(required = false) long dId, @RequestParam@Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, @Nullable@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return prescriptionService.getFilteredPrescriptions(page, size, status, pId, dId, start, end);
    }

    @GetMapping("/{id}")
    public Prescription getPrescriptionById(@PathVariable long id) {
        return prescriptionService.getPrescriptionById(id);
    }

    @PutMapping("/{id}")
    public PrescriptionDto updatePrescription(@PathVariable long id, @RequestParam PrescriptionDto dto) {
        return prescriptionService.updatePrescription(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletePrescription(@PathVariable long id) {
        prescriptionService.deletePrescription(id);
    }

    @GetMapping("/isValid/{id}")
    public Prescription isValid(@PathVariable long id) {
        return prescriptionService.isValid(id);
    }


}
