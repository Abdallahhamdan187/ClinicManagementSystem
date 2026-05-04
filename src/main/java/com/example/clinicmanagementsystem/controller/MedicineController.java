package com.example.clinicmanagementsystem.controller;

import com.example.clinicmanagementsystem.Service.MedicineService;
import com.example.clinicmanagementsystem.dto.MedicineDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicine")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    public MedicineDto add(@RequestBody MedicineDto dto) {
        return medicineService.addMedicine(dto);
    }

    @GetMapping
    public List<MedicineDto> getAll() {
        return medicineService.getAllMedicines();
    }

    @GetMapping("/{id}")
    public MedicineDto getById(@PathVariable Long id) {
        return medicineService.getMedicineById(id);
    }

    @PutMapping("/{id}")
    public MedicineDto update(@PathVariable Long id, @RequestBody MedicineDto dto) {
        return medicineService.updateMedicine(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
    }
}
