package com.example.clinicmanagementsystem.repo;

import com.example.clinicmanagementsystem.entities.Doctor;
import com.example.clinicmanagementsystem.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientRepo extends JpaRepository<Patient,Long> {

    @Query("SELECT p FROM Patient p WHERE " +
            "(:name IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:dob IS NULL OR p.dob = :dob) AND " +
            "(:email IS NULL OR p.email = :email)")
    Page<Patient> findByFilters(
            @Param("name") String name,
            @Param("dob") LocalDate dob,
            @Param("email") String email,
            Pageable pageable);

    Optional<Patient> findByEmail(String email);
}
