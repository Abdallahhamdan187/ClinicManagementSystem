package com.example.clinicmanagementsystem.Service;

import com.example.clinicmanagementsystem.dto.PrescriptionDto;
import com.example.clinicmanagementsystem.dto.PrescriptionStatus;
import com.example.clinicmanagementsystem.dto.PrescriptionsMedicineDto;
import com.example.clinicmanagementsystem.entities.Medicine;
import com.example.clinicmanagementsystem.entities.Prescription;
import com.example.clinicmanagementsystem.entities.PrescriptionsMedicine;
import com.example.clinicmanagementsystem.exception.*;
import com.example.clinicmanagementsystem.repo.*;
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
public class PrescriptionService {

    private final PrescriptionRepo prescriptionRepo;
    private final MedicineRepo medicineRepo;
    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;
    private final AppointmentRepo appointmentRepo;
    private final InvoiceRepo invoiceRepo;

    public PrescriptionDto addPrescription(@NonNull PrescriptionDto dto) {
        Prescription prescription = new Prescription();
        prescription.setIssuedAt(LocalDate.now());
        prescription.setValidUntil(dto.validUntil());
        prescription.setInstruction(dto.instruction());
        prescription.setStatus(PrescriptionStatus.Valid);

        prescription.setPatient(patientRepo.findById(dto.patientId()).orElseThrow(PatientNotFoundException::new));
        prescription.setDoctor(doctorRepo.findById(dto.doctorId()).orElseThrow(DoctorNotFoundException::new));
        prescription.setAppointment(appointmentRepo.findById(dto.appointmentId()).orElseThrow(AppointmentNotFoundException::new));

        if (dto.medicines() != null && !dto.medicines().isEmpty()) {
            List<PrescriptionsMedicine> items = dto.medicines().stream().map(itemDto -> {
                Medicine medicine = medicineRepo.findById(itemDto.medicineId()).orElseThrow(MedicineNotFoundException::new);

                PrescriptionsMedicine pm = new PrescriptionsMedicine();
                pm.setMedicine(medicine);
                pm.setDosage(itemDto.dosage());
                pm.setFreq(itemDto.freq());
                pm.setDurationDays(itemDto.durationDays());
                pm.setInstruction(itemDto.instruction());
                pm.setPrice(medicine.getPrice());
                pm.setPrescription(prescription);
                return pm;
            }).toList();

            prescription.setMedicines(items);
        }
        Prescription saved = prescriptionRepo.save(prescription);
        return new PrescriptionDto(
                saved.getId(),
                saved.getIssuedAt(),
                saved.getValidUntil(),
                saved.getInstruction(),
                null, // invoiceId
                saved.getDoctor().getId(),
                saved.getPatient().getId(),
                saved.getAppointment().getId(),
                saved.getMedicines().stream().map(m -> new PrescriptionsMedicineDto(
                        m.getId(),
                        m.getDosage(),
                        m.getFreq(),
                        m.getDurationDays(),
                        m.getInstruction(),
                        m.getPrice(),
                        saved.getId(),
                        m.getMedicine().getId()
                )).toList()
        );
    }



    public Page<Prescription> getFilteredPrescriptions(int page, int size, PrescriptionStatus status, Long pId, Long dId, LocalDate start, LocalDate end) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("issuedAt").descending());

        return prescriptionRepo.findByFilters(status, pId, dId, start, end, pageable);
    }

    public Prescription getPrescriptionById(long id) {
        return prescriptionRepo.findById(id).orElseThrow(PrescriptionNotFoundException::new);
    }

    public PrescriptionDto updatePrescription(long id, @NonNull PrescriptionDto dto) {
        Prescription prescription = prescriptionRepo.findById(id)
                .orElseThrow(PrescriptionNotFoundException::new);

        prescription.setIssuedAt(dto.issuedAt());
        prescription.setValidUntil(dto.validUntil());
        prescription.setInstruction(dto.instruction());

        if (dto.doctorId() != null) {
            prescription.setDoctor(doctorRepo.findById(dto.doctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found")));
        }
        if (dto.patientId() != null) {
            prescription.setPatient(patientRepo.findById(dto.patientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found")));
        }
        if (dto.appointmentId() != null) {
            prescription.setAppointment(appointmentRepo.findById(dto.appointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found")));
        }
        if (dto.invoiceId() != null) {
            prescription.setInvoice(invoiceRepo.findById(dto.invoiceId())
                    .orElseThrow(() -> new RuntimeException("Invoice not found")));
        }

        Prescription updated = prescriptionRepo.save(prescription);

        return new PrescriptionDto(
                updated.getId(),
                updated.getIssuedAt(),
                updated.getValidUntil(),
                updated.getInstruction(),
                updated.getInvoice().getId(),
                updated.getDoctor().getId(),
                updated.getPatient().getId(),
                updated.getAppointment().getId(),
                updated.getMedicines() != null ? updated.getMedicines().stream().map(pm -> new PrescriptionsMedicineDto(
                        pm.getId(),
                        pm.getDosage(),
                        pm.getFreq(),
                        pm.getDurationDays(),
                        pm.getInstruction(),
                        pm.getPrice(),
                        pm.getPrescription().getId(),
                        pm.getMedicine().getId()

                )).toList()
                        : null


        );
    }


    public void deletePrescription(long id) {
        Prescription findPrescription = prescriptionRepo.findById(id).orElseThrow(PrescriptionNotFoundException::new);
        prescriptionRepo.deleteById(id);

    }

    public Prescription isValid(long id) {
        return prescriptionRepo.findIfValid(id).orElseThrow(PrescriptionNotFoundException::new);
    }

    public void expire(long id) {
        Prescription prescription1 = prescriptionRepo.findById(id).orElseThrow(PrescriptionNotFoundException::new);
        prescription1.setStatus(PrescriptionStatus.Expired);
        prescriptionRepo.save(prescription1);
    }


}
