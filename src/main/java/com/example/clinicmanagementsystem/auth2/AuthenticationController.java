package com.example.clinicmanagementsystem.auth2;

import com.example.clinicmanagementsystem.AuthService.AuthDoctorService;
import com.example.clinicmanagementsystem.AuthService.AuthPatientService;
import com.example.clinicmanagementsystem.AuthService.AuthReceptionistService;
import com.example.clinicmanagementsystem.RegisterRequest.RegisterDoctorRequest;
import com.example.clinicmanagementsystem.RegisterRequest.RegisterPatientRequest;
import com.example.clinicmanagementsystem.RegisterRequest.RegisterReceptionistRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthDoctorService authDoctorService;
private final AuthPatientService authPatientService;
private final AuthReceptionistService authReceptionistService;

    @PostMapping("/register/doctor")
    public ResponseEntity<AuthenticationResponse> registerDoctor(
            @RequestBody RegisterDoctorRequest request) {
        return ResponseEntity.ok(authDoctorService.registerDoctor(request));
    }

    @PostMapping("/authenticate/Doctor")
    public ResponseEntity<AuthenticationResponse> authenticateDoctor(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authDoctorService.authenticate(request));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<AuthenticationResponse> registerPatient(
            @RequestBody RegisterPatientRequest request) {
        return ResponseEntity.ok(authPatientService.register(request));
    }


    @PostMapping("/authenticate/pateint")
    public ResponseEntity<AuthenticationResponse> authenticatePatient(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authPatientService.authenticate(request));
    }

    @PostMapping("/register/receptionist")
    public ResponseEntity<AuthenticationResponse> registerReceptionist(
            @RequestBody RegisterReceptionistRequest request) {
        return ResponseEntity.ok(authReceptionistService.register(request));
    }

    @PostMapping("/authenticate/receptionist")
    public ResponseEntity<AuthenticationResponse> authenticateReceptionist(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authReceptionistService.authenticate(request));
    }



}
