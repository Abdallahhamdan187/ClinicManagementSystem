package com.example.clinicmanagementsystem.Service;

import com.example.clinicmanagementsystem.dto.ReceptionistDto;
import com.example.clinicmanagementsystem.entities.Invoice;
import com.example.clinicmanagementsystem.entities.Receptionist;
import com.example.clinicmanagementsystem.entities.Role;
import com.example.clinicmanagementsystem.exception.PrescriptionNotFoundException;
import com.example.clinicmanagementsystem.exception.ReceptionistNotFoundException;
import com.example.clinicmanagementsystem.repo.InvoiceRepo;
import com.example.clinicmanagementsystem.repo.PatientRepo;
import com.example.clinicmanagementsystem.repo.ReceptionistRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceptionistService {

    private final InvoiceRepo invoiceRepo;
    private final ReceptionistRepo receptionistRepo;
    private final PatientRepo patientRepo;

//    public ReceptionistDto addReceptionist(@NonNull ReceptionistDto dto) {
//        Receptionist receptionist = new Receptionist();
//
//        receptionist.setFirstName(dto.firstName());
//        receptionist.setLastName(dto.lastName());
//        receptionist.setEmail(dto.email());
//        receptionist.setAge(dto.age());
//        receptionist.setRole(Role.Receptionist);
//        receptionist.setPhoneNumber(dto.phoneNumber());
//        receptionist.setUserName(dto.userName());
//        receptionist.setPassword(dto.password());
//
//        Receptionist saved = receptionistRepo.save(receptionist);
//
//        return new ReceptionistDto(
//                saved.getId(),
//                saved.getFirstName(),
//                saved.getLastName(),
//                saved.getEmail(),
//                saved.getAge(),
//                saved.getPhoneNumber(),
//                saved.getUsername(),
//                saved.getPassword(),
//                null
//
//        );
//    }


    public Page<Receptionist> getReceptionist(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return receptionistRepo.findAll(pageable);

    }

    public Receptionist getReceptionistById(long id) {
        return receptionistRepo.findById(id).orElseThrow(ReceptionistNotFoundException::new);
    }

    public ReceptionistDto updateReceptionist(long id, @NonNull ReceptionistDto dto) {
        Receptionist findReceptionist = receptionistRepo.findById(id)
                .orElseThrow(ReceptionistNotFoundException::new);

        findReceptionist.setFirstName(dto.firstName());
        findReceptionist.setEmail(dto.email());
        findReceptionist.setAge(dto.age());
        findReceptionist.setPhoneNumber(dto.phoneNumber());
        findReceptionist.setRole(Role.Receptionist);
        findReceptionist.setUserName(dto.userName());
        findReceptionist.setPassword(dto.password());

        Receptionist updated = receptionistRepo.save(findReceptionist);

        return new ReceptionistDto(
                updated.getId(),
                updated.getFirstName(),
                updated.getLastName(),
                updated.getEmail(),
                updated.getAge(),
                updated.getPhoneNumber(),
                updated.getUsername(),
                updated.getPassword(),
                null

        );
    }


    public void deleteReceptionist(long id) {
        Receptionist findReceptionist = receptionistRepo.findById(id).orElseThrow(PrescriptionNotFoundException::new);
        receptionistRepo.deleteById(id);
    }

    public Page<Invoice> checkInvoice(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return invoiceRepo.findAll(pageable);
    }


}
