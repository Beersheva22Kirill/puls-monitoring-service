package monitoring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import monitoring.dto.EmailNotificationData;
import monitoring.dto.ResponseData;
import monitoring.service.EmailDataProvider;
import monitoring.service.EmailDataProviderImpl;


@SpringBootTest
@Sql(scripts = "script-hospital-data.sql")
class EmailDataProviderTest {
private static final long PATIENT_ID = 125l;
private static final long PATIENT_ID_NO_DOCTOR = 126l;
@Autowired
EmailDataProviderImpl service;
EmailNotificationData data = new EmailNotificationData("petya@gmail.com", "Petya", "Olya");
	@Test
	void doctorPatientExistTest() {
		assertEquals(data, service.getData(PATIENT_ID));
	}
	@Test
	void patientNotFoundTest() {
		assertThrowsExactly(RuntimeException.class, ()->service.getData(10000), "patient not found");
	}
	@Test
	void doctorNotFoundTest() {
		assertThrowsExactly(RuntimeException.class, ()->service.getData(PATIENT_ID_NO_DOCTOR), "doctor not found");
	}

}
