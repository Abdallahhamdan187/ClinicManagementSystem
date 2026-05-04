package com.example.clinicmanagementsystem.config;

import com.example.clinicmanagementsystem.repo.DoctorRepo;
import com.example.clinicmanagementsystem.repo.PatientRepo;
import com.example.clinicmanagementsystem.repo.ReceptionistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final ReceptionistRepo receptionistRepo;

    private final SecurityConfigUtils securityConfigUtils;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var doctor = doctorRepo.findByEmail(username);
            if (doctor.isPresent()) return doctor.get();

            var patient = patientRepo.findByEmail(username);
            if (patient.isPresent()) return patient.get();

            var receptionist = receptionistRepo.findByEmail(username);
            if (receptionist.isPresent()) return receptionist.get();

            throw new UsernameNotFoundException("User not found across all tables");
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(securityConfigUtils.passwordEncoder());
        return authProvider;
    }
}
