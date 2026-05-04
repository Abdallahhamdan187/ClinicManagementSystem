package com.example.clinicmanagementsystem.Service;

import com.example.clinicmanagementsystem.dto.InvoiceStatus;
import com.example.clinicmanagementsystem.dto.PatientDto;
import com.example.clinicmanagementsystem.entities.*;
import com.example.clinicmanagementsystem.exception.PatientNotFoundException;
import com.example.clinicmanagementsystem.repo.InvoiceRepo;
import com.example.clinicmanagementsystem.repo.MedicalRecordRepo;
import com.example.clinicmanagementsystem.repo.PatientRepo;
import com.example.clinicmanagementsystem.repo.PrescriptionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepo patientRepo;
    private final PrescriptionRepo prescriptionRepo;
    private final MedicalRecordRepo medicalRecordRepo;
    private final InvoiceRepo invoiceRepo;


//    public PatientDto addPatient(@NonNull PatientDto dto) {
//
//        Patient patient = new Patient();
//
//        patient.setFirstName(dto.firstName());
//        patient.setLastName(dto.lastName());
//        patient.setEmail(dto.email());
//        patient.setAge(dto.age());
//        patient.setPhoneNumber(dto.phoneNumber());
//        patient.setUserName(dto.userName());
//        patient.setPassword(dto.password());
//        patient.setRole(Role.Patient);
//        patient.setDob(dto.dob());
//
//        Patient saved = patientRepo.save(patient);
//
//        return new PatientDto(
//                saved.getId(),
//                saved.getFirstName(),
//                saved.getLastName(),
//                saved.getEmail(),
//                saved.getAge(),
//                saved.getPhoneNumber(),
//                saved.getUsername(),
//                saved.getPassword(),
//                saved.getDob(),
//                null,
//                null,
//                null,
//                null
//        );
//    }


    public Page<Patient> getFilteredPatients(int page, int size, String name, LocalDate dob, String email) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());

        return patientRepo.findByFilters(name, dob, email, pageable);
    }

    public Patient getPatientById(long id) {
        return patientRepo.findById(id).orElseThrow(PatientNotFoundException::new);
    }

    public PatientDto updatePatient(long id, @NonNull PatientDto dto) {
        Patient findPatient = patientRepo.findById(id)
                .orElseThrow(PatientNotFoundException::new);

        findPatient.setFirstName(dto.firstName());
        findPatient.setLastName(dto.lastName());
        findPatient.setEmail(dto.email());
        findPatient.setAge(dto.age());
        findPatient.setPhoneNumber(dto.phoneNumber());
        findPatient.setUserName(dto.userName());
        findPatient.setPassword(dto.password());
        findPatient.setRole(Role.Patient);

        findPatient.setDob(dto.dob());

        Patient updated = patientRepo.save(findPatient);

        return new PatientDto(
                updated.getId(),
                updated.getFirstName(),
                updated.getLastName(),
                updated.getEmail(),
                updated.getAge(),
                updated.getPhoneNumber(),
                updated.getUsername(),
                updated.getPassword(),
                updated.getDob(),
                null,
                null,
                null,
                null
        );
    }


    public void deletePatient(long id) {
        Patient findPatient = patientRepo.findById(id).orElseThrow(PatientNotFoundException::new);
        patientRepo.deleteById(id);
    }

    public List<Prescription> getActivePrescription(long id) {

        return prescriptionRepo.findAllActivePrescriptionsByPatientId(id);

    }

    public List<MedicalRecord> getMedicalHistory(long id) {

        return medicalRecordRepo.findByPatientId(id);
    }

    public List<Invoice> getUnPaidInvoices(long id) {
        return invoiceRepo.getUnPaidInvoicesByPatientId(id, InvoiceStatus.notPAID);
    }


}
