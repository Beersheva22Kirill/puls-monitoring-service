package monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import monitoring.dto.PulseProbe;
import monitoring.entities.ProbesList;
import monitoring.repo.ProbesListsRepository;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class AvgReducerTest {
	static final long PATIENT_ID_NO_REDIS = 123L;
	static final long PATIENT_ID_NO_AVG = 124l;
	static final long PATIENT_ID_AVG = 125L;
	static final int VALUE1 = 100;
	static final int VALUE2 = 120;
	static final int AVG_VALUE = (VALUE1 + VALUE2) / 2;
	static PulseProbe probeNoRedis = new PulseProbe(PATIENT_ID_NO_REDIS, VALUE1, 0, 0);
	static PulseProbe probeNoAvg = new PulseProbe(PATIENT_ID_NO_AVG, VALUE1, 0, 0);
	static PulseProbe probeAvg = new PulseProbe(PATIENT_ID_AVG, VALUE2, 0, 0);
	static ProbesList probesListNoAvg = new ProbesList(PATIENT_ID_NO_AVG);
	static ProbesList probesListAvg = new ProbesList(PATIENT_ID_AVG);
	static List<Integer> emptyList;
	static List<Integer> oneValueList;
	
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	@MockBean
	ProbesListsRepository probesListRepository;
	String consumerBindingName = "pulseProbeConsumer-in-0";
	@Value("${app.avg.binding.name}")
	String producerBindingName;
	
	@BeforeAll
	static void setUpProbesLists() {
		emptyList = probesListNoAvg.getPulseValues();
		oneValueList = probesListAvg.getPulseValues();
		oneValueList.add(VALUE1);
	}
	@BeforeEach
	void redisMocking() {
		when(probesListRepository.findById(PATIENT_ID_NO_REDIS)).thenReturn(Optional.ofNullable(null));
		when(probesListRepository.findById(PATIENT_ID_NO_AVG)).thenReturn(Optional.of(probesListNoAvg));
		when(probesListRepository.findById(PATIENT_ID_AVG)).thenReturn(Optional.of(probesListAvg));
		
	}
	@Test
	void noRedisDataTest() {
		
		producer.send(new GenericMessage<PulseProbe>(probeNoRedis), consumerBindingName);
		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertNull(message);
	}
	@Test
	void noAvgDataTest() {
		
		producer.send(new GenericMessage<PulseProbe>(probeNoAvg), consumerBindingName);
		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertNull(message);
		assertEquals(1, emptyList.size());
	}
	@Test
	void avgDataTest() throws Exception{
		producer.send(new GenericMessage<PulseProbe>(probeAvg), consumerBindingName);
		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		PulseProbe actualProbe = mapper.readValue(message.getPayload(), PulseProbe.class);
		assertEquals(PATIENT_ID_AVG, actualProbe.patientId());
		assertEquals(AVG_VALUE, actualProbe.value());
		long currentTime = System.currentTimeMillis();
		long probeTime = actualProbe.timeStamp();
		assertTrue(probeTime > 0 && probeTime <= currentTime);
	}
	
	

}
