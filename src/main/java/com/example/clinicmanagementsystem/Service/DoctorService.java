package com.example.clinicmanagementsystem.Service;

import com.example.clinicmanagementsystem.dto.DoctorDto;
import com.example.clinicmanagementsystem.entities.Appointment;
import com.example.clinicmanagementsystem.entities.Doctor;
import com.example.clinicmanagementsystem.entities.Invoice;
import com.example.clinicmanagementsystem.entities.Role;
import com.example.clinicmanagementsystem.exception.DoctorNotFoundException;
import com.example.clinicmanagementsystem.repo.AppointmentRepo;
import com.example.clinicmanagementsystem.repo.DoctorRepo;
import com.example.clinicmanagementsystem.repo.InvoiceRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepo doctorRepo;
    private final AppointmentRepo appointmentRepo;
    private final InvoiceRepo invoiceRepo;

//    public DoctorDto addDoctor(@NonNull DoctorDto dto) {
//        Doctor doctor = new Doctor();
//
//        doctor.setFirstName(dto.firstName());
//        doctor.setLastName(dto.lastName());
//        doctor.setEmail(dto.email());
//        doctor.setAge(dto.age());
//        doctor.setPhoneNumber(dto.phoneNumber());
//        doctor.setUserName(dto.userName());
//        doctor.setPassword(dto.password());
//        doctor.setRole(Role.Doctor);
//        doctor.setSpecialization(dto.specialization());
//        doctor.setStartTime(dto.startTime());
//        doctor.setEndTime(dto.endTime());
//
//        Doctor saved = doctorRepo.save(doctor);
//
//        return new DoctorDto(
//                saved.getId(),
//                saved.getFirstName(),
//                saved.getLastName(),
//                saved.getEmail(),
//                saved.getAge(),
//                saved.getPhoneNumber(),
//                saved.getUsername(),
//                saved.getPassword(),
//                saved.getStartTime(),
//                saved.getEndTime(),
//                saved.getSpecialization(),
//                null,
//                null,
//                null
//        );
//
//    }
//

    public Page<Doctor> getDoctors(int page, int size, String spec, String name) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return doctorRepo.findByFilters(spec, name, pageable);
    }

    public Doctor getDoctorById(long id) {
        return doctorRepo.findById(id).orElseThrow(RuntimeException::new);
    }

    public DoctorDto updateDoctor(long id, @NonNull DoctorDto dto) {
        Doctor findDoctor = doctorRepo.findById(id)
                .orElseThrow(DoctorNotFoundException::new);

        findDoctor.setFirstName(dto.firstName());
        findDoctor.setLastName(dto.lastName());
        findDoctor.setEmail(dto.email());
        findDoctor.setAge(dto.age());
        findDoctor.setPhoneNumber(dto.phoneNumber());
        findDoctor.setUserName(dto.userName());
        findDoctor.setPassword(dto.password());
findDoctor.setRole(Role.Doctor);
        findDoctor.setSpecialization(dto.specialization());
        findDoctor.setStartTime(dto.startTime());
        findDoctor.setEndTime(dto.endTime());


        Doctor updated = doctorRepo.save(findDoctor);

        return new DoctorDto(
                updated.getId(),
                updated.getFirstName(),
                updated.getLastName(),
                updated.getEmail(),
                updated.getAge(),
                updated.getPhoneNumber(),
                updated.getUsername(),
                updated.getPassword(),
                updated.getStartTime(),
                updated.getEndTime(),
                updated.getSpecialization(),
                null,
                null,
                null
        );
    }


    public void deleteDoctor(long id) {
        Doctor findDoctor = doctorRepo.findById(id).orElseThrow(RuntimeException::new);
        doctorRepo.deleteById(id);

    }

    public List<Appointment> getSchedule(Long id) {
        doctorRepo.findById(id).orElseThrow(DoctorNotFoundException::new);

        return appointmentRepo.getSchedule(id);
    }


    public int getTotalEarning(long doctorId) {
        if (!doctorRepo.existsById(doctorId)) {
            throw new DoctorNotFoundException();
        }
        Integer total = invoiceRepo.sumAmountByDoctorId(doctorId);

        return (total != null) ? total : 0;
    }

    public double getTodayEarning(long id) {
        if (!doctorRepo.existsById(id)) {
            throw new DoctorNotFoundException();
        }
        return invoiceRepo.sumTodayEarningByDoctorId(id);
    }

    public double getEarningFromRange(long id, LocalDate startTime, LocalDate endTime) {
        if (!doctorRepo.existsById(id)) {
            throw new DoctorNotFoundException();
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        return invoiceRepo.sumEarningsByDoctorAndRange(id, startTime, endTime);
    }

    public List<Appointment> getAppointmentsFromRange(long id, LocalDate startTime, LocalDate endTime) {
        if (!doctorRepo.existsById(id)) {
            throw new DoctorNotFoundException();
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        return appointmentRepo.getAppointmentsFromRange(id, startTime, endTime);
    }

    public List<Invoice> getInvoices() {
        return invoiceRepo.findAll();
    }

}
