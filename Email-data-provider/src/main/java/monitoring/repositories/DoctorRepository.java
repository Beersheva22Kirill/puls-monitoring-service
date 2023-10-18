package monitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import monitoring.entities.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
