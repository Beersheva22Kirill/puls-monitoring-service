package monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.RestTemplate;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import monitoring.dto.EmailNotificationData;
import monitoring.dto.JumpPulse;
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class JumpsNotifierTest {
	
	static final long PATIENT_ID = 123l;
	static final String PATIENT_NAME = "Vasya";
	static final String DOCTOR_EMAIL = "moshe@gmail.com";
	static final String DOCTOR_NAME = "Moshe";
	static final int PREV_VALUE = 70;
	static final int CURRENT_VALUE = 140;

	@Value("${app.email.service.name}")
	private String hospitalServiceName;
	@Value("${app.email.service.address}")
	private String hospitalServiceMail;

	@RegisterExtension
	static GreenMailExtension mailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
	.withConfiguration(GreenMailConfiguration.aConfig().withUser("pulse", "12345.com"));
	
	ResponseEntity<EmailNotificationData> responseNormal = new ResponseEntity<>(new EmailNotificationData(DOCTOR_EMAIL, DOCTOR_NAME, PATIENT_NAME),
		HttpStatus.OK);
	
	ResponseEntity<EmailNotificationData> responseAbnormal = new ResponseEntity<>(null,
		HttpStatus.NOT_FOUND);

	@Autowired
	InputDestination producer;
	@MockBean
	RestTemplate restTemplate;
	
	@Test
	void normalFlow() throws Exception {
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
		.thenReturn(responseNormal);
		producer.send(new GenericMessage<JumpPulse>(new JumpPulse(PATIENT_ID, PREV_VALUE, CURRENT_VALUE)));
		MimeMessage[] messages = mailExtension.getReceivedMessages();
		assertTrue(messages.length > 0);
		MimeMessage message = messages[0];
		assertEquals(DOCTOR_EMAIL, message.getAllRecipients()[0].toString());
		assertTrue(message.getSubject().contains("" + PATIENT_ID));
		String text = message.getContent().toString();
		assertTrue(text.contains(DOCTOR_NAME));
		
		
	}
	
	@Test
	void abnormalFlow() throws Exception {
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
		.thenThrow(new RuntimeException("doctor not found"));
		producer.send(new GenericMessage<JumpPulse>(new JumpPulse(PATIENT_ID, PREV_VALUE, CURRENT_VALUE)));
		MimeMessage[] messages = mailExtension.getReceivedMessages();
		assertTrue(messages.length > 0);
		MimeMessage message = messages[0];
		assertEquals(hospitalServiceMail, message.getAllRecipients()[0].toString());
		assertTrue(message.getSubject().contains("" + PATIENT_ID));
		String text = message.getContent().toString();
		assertTrue(text.contains(hospitalServiceName));
		
		
	}

}
