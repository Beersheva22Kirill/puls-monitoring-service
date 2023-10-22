package monitoring.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import monitoring.entities.Doctor;
import monitoring.entities.Patient;
import monitoring.entities.Visit;
import monitoring.repositories.DoctorRepository;
import monitoring.repositories.PatientRepository;
import monitoring.repositories.VisitRepository;

@Service
@Slf4j
public class RandomDataBaseService {
	
	@Value("${app.database.random.count.doctor:5}")
	int countDoctors;
	@Value("${app.database.random.count.patient:20}")
	int countPatients;
	@Value("${app.database.random.count.visit:100}")
	int countVisits;
	@Value("${app.database.random.create.status}")
	boolean create;
	
	@Autowired
	DoctorRepository doctorRepo;
	@Autowired
	PatientRepository patientRepo;
	@Autowired
	VisitRepository visitRepo;
	
	public void createRandomDataBase() {
		if(create) {
			log.info("Creation random databases begin");
			createDoctors();
			createPatients();
			createVisits();
			log.info("Creation random databases end");
		}
	}
	@Transactional
	private void createDoctors() {
		List<Doctor> list = new ArrayList<Doctor>();
		for (int i = 1; i <= countDoctors; i++) {
			Doctor doctor = new Doctor(i, String.format("doctor%d@hospital.com", i), String.format("Doctor_%d", i));
			list.add(doctor);
		}
		doctorRepo.saveAll(list);
		log.info("Creation doctors completed");
	}
	
	@Transactional
	private void createPatients() {
		List<Patient> list = new ArrayList<Patient>();
		for (int i = countDoctors + 1; i <= countPatients + countDoctors; i++) {
			Patient patient = new Patient(i, String.format("patient%d@hospital.com", i), String.format("Patient_%d", i));
			list.add(patient);
		}
		patientRepo.saveAll(list);
		log.info("Creation patient completed");
	}
	
	@Transactional
	private void createVisits() {
		List<Visit> list = new ArrayList<Visit>();
		for (int i = 1; i <= countVisits; i++) {
			LocalDate dateVisit = generateRandomDate();
			Doctor doctor = doctorRepo.findById((long) generateRandomInt(1,countDoctors + 1)).orElse(null);
			Patient patient = patientRepo.findById((long) generateRandomInt(countDoctors + 1,countPatients + countDoctors + 1)).orElse(null);
			list.add(new Visit(doctor, patient, dateVisit));
		}
		visitRepo.saveAll(list);
		log.info("Creation visits completed");
	}

	private int generateRandomInt(int min, int max) {
		
		return new Random().nextInt(min,max);
	}

	private LocalDate generateRandomDate() {
		
		int year = generateRandomInt(2020, 2024);
		int mounth = generateRandomInt(1, 13);
		int day = generateRandomInt(1, 29);
		
		return LocalDate.of(year, mounth, day);
	}


}
