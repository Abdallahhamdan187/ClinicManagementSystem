package com.example.clinicmanagementsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends Human {
    @Column(nullable = false)
    private String specialization;
    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalTime endTime;

    @OneToMany(mappedBy = "doctor" ,orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointment;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "prescriptionId")
    @JsonIgnore
    private List<Prescription> prescriptions;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "invoiceId")
    @JsonIgnore
    private List<Invoice> invoices;
}
