package com.example.clinicmanagementsystem.controller;

import com.example.clinicmanagementsystem.Service.AppointmentService;
import com.example.clinicmanagementsystem.Service.PatientService;
import com.example.clinicmanagementsystem.Service.ReceptionistService;
import com.example.clinicmanagementsystem.dto.AppointmentDto;
import com.example.clinicmanagementsystem.dto.PatientDto;
import com.example.clinicmanagementsystem.dto.ReceptionistDto;
import com.example.clinicmanagementsystem.entities.Appointment;
import com.example.clinicmanagementsystem.entities.Invoice;
import com.example.clinicmanagementsystem.entities.Patient;
import com.example.clinicmanagementsystem.entities.Receptionist;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/reception")
@RequiredArgsConstructor
public class ReceptionistController {

    private final ReceptionistService receptionistService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;


//    @PostMapping
//    public ReceptionistDto addReceptionist(@Valid@RequestBody ReceptionistDto dto) {
//        return receptionistService.addReceptionist(dto);
//    }

    @GetMapping
    public Page<Receptionist> getAllReceptionists(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return receptionistService.getReceptionist(page, size);
    }

    @GetMapping("/{id}")
    public Receptionist getById(@PathVariable long id) {
        return receptionistService.getReceptionistById(id);
    }

    @PutMapping("/{id}")
    public ReceptionistDto updateReceptionist(@PathVariable long id, @RequestBody ReceptionistDto dto) {
        return receptionistService.updateReceptionist(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteReceptionist(@PathVariable long id) {
        receptionistService.deleteReceptionist(id);

    }

    @GetMapping("/invoices")
    public Page<Invoice> checkAllInvoices(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return receptionistService.checkInvoice(page, size);
    }


//    @PostMapping("/addPatient")
//    public PatientDto addPatient(@Valid @RequestBody PatientDto dto) {
//        return patientService.addPatient(dto);
//    }

    @GetMapping("/getPatient")
    public Page<Patient> getFilteredPatients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam @Nullable String name, @RequestParam@Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,@Nullable @RequestParam String email) {
        return patientService.getFilteredPatients(page, size, name, dob, email);
    }

    @GetMapping("/patient/{id}")
    public Patient getPatientById(@PathVariable long id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/patient/{id}")
    public PatientDto updatePatient(@PathVariable long id, @RequestBody PatientDto dto) {
        return patientService.updatePatient(id, dto);
    }

    @DeleteMapping("/patient/{id}")
    public void deletePatient(@PathVariable long id) {
        patientService.deletePatient(id);
    }

    @PostMapping("/book")
    public AppointmentDto book(@Valid @RequestBody AppointmentDto dto) {
        return appointmentService.book(dto);
    }

    @PutMapping ("/{id}/cancelAppointment")
    public void cancel(@PathVariable Long id) {
        appointmentService.cancel(id);

    }

    @PutMapping ("/{id}/complete")
    public void complete(@PathVariable Long id) {
        appointmentService.complete(id);
    }

    @PutMapping("/{id}/reschedule")
    public void reschedule(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime newStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime newEnd) {

        appointmentService.reschedule(id, newDate, newStart, newEnd);

    }

    @DeleteMapping("/{id}/appointments")
    public void delete(@PathVariable Long id) {
        appointmentService.delete(id);
    }

    @GetMapping("/appointments")
    public Page<AppointmentDto> getAppointment(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size){
       return appointmentService.getAppointments(page, size);
    }
}
