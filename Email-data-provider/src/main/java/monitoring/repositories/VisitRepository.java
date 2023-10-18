package monitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import monitoring.dto.ResponseData;
import monitoring.entities.DoctorId;
import monitoring.entities.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
	
	@Query(value = "SELECT doctor.name as doctorname, patient.name as patientname, doctor.email "
			+ "FROM visits v "
			+ "LEFT JOIN person_data doctor ON doctor.id = v.doctor_id "
			+ "LEFT JOIN person_data patient ON patient.id = v.patient_id "
			+ "WHERE "
			+ "v.patient_id = :patientId "
			+ "ORDER BY "
			+ "v.date_visit desc "
			+ "LIMIT 1", nativeQuery = true)
	ResponseData findDoctorByLastVisit(long patientId);
	
	@Query(value="select doctor_id as id "
			+ "from visits "
			+ "where patient_id=:patientId "
			+ "order by date desc "
			+ "limit 1", nativeQuery = true)
	DoctorId findDoctorIdLastVisit(long patientId);
	

}
