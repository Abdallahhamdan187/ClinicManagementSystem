package com.example.clinicmanagementsystem.repo;

import com.example.clinicmanagementsystem.entities.Doctor;
import com.example.clinicmanagementsystem.entities.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceptionistRepo extends JpaRepository<Receptionist,Long> {


    Optional<Receptionist> findByEmail(String email);

}
