package monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

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

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import monitoring.dto.JumpPulse;
import monitoring.dto.PulseProbe;
import monitoring.entity.LastPulsValue;
import monitoring.repo.LastPulseValueRepo;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class JumpsDetectorTest {

	static final long PATIENT_ID_NO_REDIS = 123L;
	static final long PATIENT_ID_NO_JUMP = 124L;
	static final long PATIENT_ID_JUMP = 125L;
	private static final int VALUE = 100;
	private static final int JUMP_VALUE = 300;
	
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	
	PulseProbe probeNoRedis = new PulseProbe(PATIENT_ID_NO_REDIS, VALUE, 0, 0);
	PulseProbe probeNoJump = new PulseProbe(PATIENT_ID_NO_JUMP, VALUE, 0, 0);
	PulseProbe probeJump = new PulseProbe(PATIENT_ID_JUMP, JUMP_VALUE, 0, 0);
	LastPulsValue noJumpValue = new LastPulsValue(PATIENT_ID_NO_JUMP, VALUE);
	LastPulsValue jumpValue = new LastPulsValue(PATIENT_ID_JUMP, VALUE);
	JumpPulse jumpPulse = new JumpPulse(PATIENT_ID_JUMP, VALUE, JUMP_VALUE);
	
	String consumerBindingName = "pulseProbeConsumerJumps-in-0";
	@Value("${app.jumps.binding.name}")
	String producerBindingName;
	
	@MockBean
	LastPulseValueRepo lastPulseRepo;
	
	@BeforeEach
	void redisMocking() {
		when(lastPulseRepo.findById(PATIENT_ID_NO_REDIS)).thenReturn(Optional.ofNullable(null));
		when(lastPulseRepo.findById(PATIENT_ID_NO_JUMP)).thenReturn(Optional.ofNullable(noJumpValue));
		when(lastPulseRepo.findById(PATIENT_ID_JUMP)).thenReturn(Optional.ofNullable(jumpValue));
	}
	
	@Test
	void noRedisTest() {
		producer.send(new GenericMessage<PulseProbe>(probeNoJump),consumerBindingName);
		Message<byte[]> message = consumer.receive(10,producerBindingName);
		assertNull(message);
	}
	
	@Test
	void noJumpTest() {
		producer.send(new GenericMessage<PulseProbe>(probeNoRedis),consumerBindingName);
		Message<byte[]> message = consumer.receive(10,producerBindingName);
		assertNull(message);
	}
	
	@Test
	void jumpTest() throws Exception {
		producer.send(new GenericMessage<PulseProbe>(probeJump),consumerBindingName);
		Message<byte[]> message = consumer.receive(10,producerBindingName);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		JumpPulse jumpActual = mapper.readValue(message.getPayload(),JumpPulse.class);
		assertEquals(jumpActual, jumpPulse);
	}
	
	

}
