package com.example.clinicmanagementsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
@Entity
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends Human {
    @Column(nullable = false)
    private LocalDate dob;

    @OneToMany(mappedBy = "patient",orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointmentEntities;
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "invoicesId")
    @JsonIgnore
    private List<Invoice> invoices;
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "prescriptionsId")
    @JsonIgnore
    private List<Prescription> prescriptions;
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "medicalRecordsId")
    @JsonIgnore
    private List<MedicalRecord> medicalRecords;



}
