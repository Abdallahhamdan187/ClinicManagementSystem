package com.example.clinicmanagementsystem.AuthService;


import com.example.clinicmanagementsystem.RegisterRequest.RegisterReceptionistRequest;
import com.example.clinicmanagementsystem.auth2.AuthRequest;
import com.example.clinicmanagementsystem.auth2.AuthenticationResponse;
import com.example.clinicmanagementsystem.config.JwtService;
import com.example.clinicmanagementsystem.config.SecurityConfigUtils;
import com.example.clinicmanagementsystem.entities.Receptionist;
import com.example.clinicmanagementsystem.entities.Role;
import com.example.clinicmanagementsystem.repo.ReceptionistRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthReceptionistService {
    private final ReceptionistRepo receptionistRepo;
    private final JwtService jwtService;
    private final SecurityConfigUtils securityConfigUtils;
    private final AuthenticationManager manager;

    public AuthenticationResponse register(RegisterReceptionistRequest request) {
        Receptionist receptionist = Receptionist.builder()
                .email(request.getEmail())
                .password(securityConfigUtils.passwordEncoder().encode(request.getPassword()))
                .role(Role.Receptionist)
                .age(request.getAge())
                .userName(request.getUserName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        receptionistRepo.save(receptionist);
        String token = jwtService.generateToken(receptionist);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthRequest request) {
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = receptionistRepo.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}