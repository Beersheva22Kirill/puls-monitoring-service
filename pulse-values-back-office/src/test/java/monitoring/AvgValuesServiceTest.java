package monitoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import monitoring.documents.AvgPulseDoc;
import monitoring.dto.PulseProbe;
import monitoring.repositoies.*;
import monitoring.service.AvgPulseValuesService;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AvgValuesServiceTest {
	private static final long PATIENT_ID = 123;
	private static final int VALUE1 = 60;
	private static final int VALUE2 = 80;
	private static final int VALUE3 = 100;
	private static final int AVG_PULSE_VALUE = 80;
	private static final int MIN_PULSE_VALUE = 60;
	private static final int MAX_PULSE_VALUE = 100;
	private static final long PATIENT_ID_1 = 124;
	private static final int VALUE4 = 150;
	
	@Autowired
	AvgPulseRepo avgPulseRepo;
	@Autowired
	AvgPulseValuesService avgValuesService;
	
	PulseProbe probe1 = new PulseProbe(PATIENT_ID, VALUE1, System.currentTimeMillis(), 0);
	PulseProbe probe2 = new PulseProbe(PATIENT_ID, VALUE2, System.currentTimeMillis(), 0);
	PulseProbe probe3 = new PulseProbe(PATIENT_ID, VALUE3, System.currentTimeMillis(), 0);
	PulseProbe probe4 = new PulseProbe(PATIENT_ID_1, VALUE4, System.currentTimeMillis(), 0);
	@Test
	@Order(1)
	void addValues() {
		avgPulseRepo.save(AvgPulseDoc.of(probe1));
		avgPulseRepo.save(AvgPulseDoc.of(probe2));
		avgPulseRepo.save(AvgPulseDoc.of(probe3));
		avgPulseRepo.save(AvgPulseDoc.of(probe4));
	}
	@Test
	void avgValuesTest() {
		assertEquals(AVG_PULSE_VALUE, avgValuesService.getAvgValue(PATIENT_ID, LocalDateTime.now().minusHours(10),LocalDateTime.now().plusHours(10)));
		assertEquals(0, avgValuesService.getAvgValue(PATIENT_ID,
				LocalDateTime.of(2020, 1, 1, 1, 1),
				LocalDateTime.of(2021, 1, 1, 1, 1)));
		assertEquals(VALUE4, avgValuesService.getAvgValue(PATIENT_ID_1,LocalDateTime.now().minusHours(10),LocalDateTime.now().plusHours(10)));
	}
	@Test
	void minValuesTest() {
		assertEquals(MIN_PULSE_VALUE, avgValuesService.getMinValue(PATIENT_ID, LocalDateTime.now().minusHours(10),LocalDateTime.now().plusHours(10)));
		assertEquals(0, avgValuesService.getMinValue(PATIENT_ID,
				LocalDateTime.of(2020, 1, 1, 1, 1),
				LocalDateTime.of(2021, 1, 1, 1, 1)));
		assertEquals(VALUE4, avgValuesService.getMinValue(PATIENT_ID_1,LocalDateTime.now().minusHours(10),LocalDateTime.now().plusHours(10)));
	}
	@Test
	void maxValuesTest() {
		assertEquals(MAX_PULSE_VALUE, avgValuesService.getMaxValue(PATIENT_ID, LocalDateTime.now().minusHours(10),LocalDateTime.now().plusHours(10)));
		assertEquals(0, avgValuesService.getMaxValue(PATIENT_ID,
				LocalDateTime.of(2020, 1, 1, 1, 1),
				LocalDateTime.of(2021, 1, 1, 1, 1)));
		assertEquals(VALUE4, avgValuesService.getMaxValue(PATIENT_ID_1,LocalDateTime.now().minusHours(10),LocalDateTime.now().plusHours(10)));
	}
	@Test
	void allValuesTest() {
		List<Integer> values = avgValuesService.getAllValues(PATIENT_ID, LocalDateTime.now().minusHours(10),LocalDateTime.now().plusHours(10));
		assertEquals(3, values.size());
		Integer[] expected = {VALUE1, VALUE2, VALUE3};
		assertArrayEquals(expected, values.toArray(Integer[]::new));
	}
}
