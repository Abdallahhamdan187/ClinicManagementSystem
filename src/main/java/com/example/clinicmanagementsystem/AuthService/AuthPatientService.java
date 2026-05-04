package com.example.clinicmanagementsystem.AuthService;

import com.example.clinicmanagementsystem.RegisterRequest.RegisterPatientRequest;
import com.example.clinicmanagementsystem.auth2.AuthRequest;
import com.example.clinicmanagementsystem.auth2.AuthenticationResponse;
import com.example.clinicmanagementsystem.config.JwtService;
import com.example.clinicmanagementsystem.config.SecurityConfigUtils;
import com.example.clinicmanagementsystem.entities.Patient;
import com.example.clinicmanagementsystem.entities.Role;
import com.example.clinicmanagementsystem.repo.PatientRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthPatientService {

        private final PatientRepo patientRepo;
        private final JwtService jwtService;
        private final SecurityConfigUtils securityConfigUtils;
        private final AuthenticationManager manager;

        public AuthenticationResponse register(RegisterPatientRequest request) {
            Patient patient = Patient.builder()
                    .email(request.getEmail())
                    .password(securityConfigUtils.passwordEncoder().encode(request.getPassword()))
                    .role(Role.Patient)
                    .age(request.getAge())
                    .userName(request.getUserName())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phoneNumber(request.getPhoneNumber())
                    .dob(request.getDob())
                    .build();

            patientRepo.save(patient);
            String token = jwtService.generateToken(patient);
            return new AuthenticationResponse(token);
        }

        public AuthenticationResponse authenticate(AuthRequest request) {
            manager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = patientRepo.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }
