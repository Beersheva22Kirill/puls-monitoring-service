package monitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import monitoring.dto.EmailNotificationData;
import monitoring.dto.ResponseData;
import monitoring.entities.Doctor;
import monitoring.entities.DoctorId;
import monitoring.entities.Patient;
import monitoring.repositories.DoctorRepository;
import monitoring.repositories.PatientRepository;
import monitoring.repositories.VisitRepository;

@Service
@Slf4j
public class EmailDataProviderImpl implements EmailDataProvider {
	
	@Autowired
	VisitRepository visitRepo;
	@Autowired
	PatientRepository patientRepo;
	@Autowired
	DoctorRepository doctorRepo;

	public EmailNotificationData getData(long patientId) {
		Patient patient = patientRepo.findById(patientId).orElseThrow(() -> new RuntimeException("patient not found"));
		DoctorId doctorId = visitRepo.findDoctorIdLastVisit(patientId);
		if(doctorId == null || doctorId.getId() == null) {
			throw new RuntimeException("doctor not found");
		}
		log.trace("doctorId is {}", doctorId.getId());
		Doctor doctor = doctorRepo.findById(doctorId.getId()).orElseThrow(() -> new IllegalStateException("doctor not found"));
		return new EmailNotificationData(doctor.getEmail(), doctor.getName(), patient.getName());
	}


}
