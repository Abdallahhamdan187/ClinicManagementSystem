package com.example.clinicmanagementsystem.Service;

import com.example.clinicmanagementsystem.dto.AppointmentDto;
import com.example.clinicmanagementsystem.dto.AppointmentStats;
import com.example.clinicmanagementsystem.entities.Appointment;
import com.example.clinicmanagementsystem.entities.Doctor;
import com.example.clinicmanagementsystem.exception.AppointmentNotFoundException;
import com.example.clinicmanagementsystem.exception.DoctorNotFoundException;
import com.example.clinicmanagementsystem.exception.PatientNotFoundException;
import com.example.clinicmanagementsystem.exception.ReceptionistNotFoundException;
import com.example.clinicmanagementsystem.repo.AppointmentRepo;
import com.example.clinicmanagementsystem.repo.DoctorRepo;
import com.example.clinicmanagementsystem.repo.PatientRepo;
import com.example.clinicmanagementsystem.repo.ReceptionistRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Slf4j
@RequiredArgsConstructor

public class AppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final ReceptionistRepo receptionistRepo;

    public boolean hasConflict(Long doctorId, LocalDate date, LocalTime start, LocalTime end) {

        return appointmentRepo.hasConflict(doctorId, date, start, end, AppointmentStats.CANCELLED);
    }

    public AppointmentDto book(@NonNull AppointmentDto dto) {
        Doctor doctor = doctorRepo.findById(dto.doctorId())
                .orElseThrow(DoctorNotFoundException::new);

        if (hasConflict(dto.doctorId(), dto.date(), dto.startTime(), dto.endTime())) {
            log.warn("Booked Slot with this date {} and start time {} ,end time {}", dto.date(), dto.startTime(), dto.endTime());
            throw new RuntimeException("Choose another Time slot, this one is already booked!!");
        }
        if (dto.startTime().isBefore(doctor.getStartTime()) ||
                dto.endTime().isAfter(doctor.getEndTime())) {
            log.warn("Out of the doctors time ");
            throw new RuntimeException("Doctor is only available between " +
                    doctor.getStartTime() + " and " + doctor.getEndTime());
        }
        if (dto.startTime().isAfter(dto.endTime())) {
            log.warn("End time {} is after start time {}", dto.endTime(), dto.startTime());
            throw new RuntimeException("Invalid time range: start time must be before end time");
        }
        if (dto.date().isBefore(LocalDate.now())) {
            log.warn("Booked in the past must be after {}", LocalDate.now());
            throw new RuntimeException("Booking must be for today or in the future");
        }

        Appointment appointment = new Appointment();
        appointment.setDate(dto.date());
        appointment.setStartTime(dto.startTime());
        appointment.setEndTime(dto.endTime());
        appointment.setStatus(dto.stats() != null ? dto.stats() : AppointmentStats.BOOKED);

        appointment.setDoctor(doctorRepo.findById(dto.doctorId())
                .orElseThrow(DoctorNotFoundException::new));
        appointment.setPatient(patientRepo.findById(dto.patientId())
                .orElseThrow(PatientNotFoundException::new));
        appointment.setReceptionist(receptionistRepo.findById(dto.receptionistId())
                .orElseThrow(ReceptionistNotFoundException::new));

        Appointment saved = appointmentRepo.save(appointment);
        log.info("Appointment was created successfully with id {} !!", dto.id());
        return new AppointmentDto(
                saved.getId(),
                saved.getDate(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.getStatus(),
                saved.getDoctor().getId(),
                saved.getPatient().getId(),
                saved.getReceptionist().getId()
        );
    }

    public Page<AppointmentDto> getAppointments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetched all Appointments");
        return appointmentRepo.findAll(pageable)
                .map(app -> new AppointmentDto(
                        app.getId(),
                        app.getDate(),
                        app.getStartTime(),
                        app.getEndTime(),
                        app.getStatus(),
                        app.getDoctor().getId(),
                        app.getPatient().getId(),
                        app.getReceptionist().getId()


                ));
    }


    public void cancel(Long id) {

        Appointment app = appointmentRepo.findById(id).orElseThrow(() -> {
            log.warn("Failed to cancel: Appointment ID {} not found", id);
            return new AppointmentNotFoundException();
        });
        app.setStatus(AppointmentStats.CANCELLED);
        appointmentRepo.save(app);
        log.info("Appointment was canceled successfully!!");
    }

    public void delete(Long id) {

        Appointment app = appointmentRepo.findById(id).orElseThrow(() -> {
            log.warn("Failed to delete: Appointment ID {} not found", id);
            return new AppointmentNotFoundException();
        });
        appointmentRepo.deleteById(id);
        log.info("Appointment was deleted successfully!!");

    }

    public void complete(Long id) {

        Appointment app = appointmentRepo.findById(id).orElseThrow(() -> {
            log.warn("Failed to complete: Appointment ID {} not found", id);
            return new AppointmentNotFoundException();
        });
        app.setStatus(AppointmentStats.COMPLETED);
        appointmentRepo.save(app);
        log.info("Appointment was completed successfully!!");
    }

    public void reschedule(Long id, LocalDate newTime, LocalTime newStart, LocalTime newEnd) {

        Appointment app = appointmentRepo.findById(id).orElseThrow(() -> {
            log.warn("Failed to reschedule: Appointment ID {} not found", id);
            return new AppointmentNotFoundException();
        });

        app.setDate(newTime);
        app.setStartTime(newStart);
        app.setEndTime(newEnd);
        appointmentRepo.save(app);
        log.info("Appointment was rescheduled successfully!!");
    }


}
