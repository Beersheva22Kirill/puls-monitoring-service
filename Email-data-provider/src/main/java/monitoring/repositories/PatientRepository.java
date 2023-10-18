package monitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import monitoring.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
