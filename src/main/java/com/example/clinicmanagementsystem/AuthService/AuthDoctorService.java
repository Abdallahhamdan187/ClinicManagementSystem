package com.example.clinicmanagementsystem.AuthService;

import com.example.clinicmanagementsystem.RegisterRequest.RegisterDoctorRequest;
import com.example.clinicmanagementsystem.auth2.AuthRequest;
import com.example.clinicmanagementsystem.auth2.AuthenticationResponse;
import com.example.clinicmanagementsystem.config.JwtService;
import com.example.clinicmanagementsystem.config.SecurityConfigUtils;
import com.example.clinicmanagementsystem.entities.Doctor;
import com.example.clinicmanagementsystem.entities.Role;
import com.example.clinicmanagementsystem.repo.DoctorRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthDoctorService {

    private final DoctorRepo doctorRepo;
    private final JwtService jwtService;
    private final SecurityConfigUtils securityConfigUtils;

    private final AuthenticationManager manager;

    public AuthenticationResponse registerDoctor(RegisterDoctorRequest request) {

        Doctor doctor = Doctor.builder()
                .email(request.getEmail())
                .password(securityConfigUtils.passwordEncoder().encode(request.getPassword()))
                .specialization(request.getSpecialization())
                .role(Role.Doctor)
                .age(request.getAge())
                .userName(request.getUserName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        doctorRepo.save(doctor);
        String token = jwtService.generateToken(doctor);

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthRequest request) {

        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()

                )
        );
        var user = doctorRepo.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
