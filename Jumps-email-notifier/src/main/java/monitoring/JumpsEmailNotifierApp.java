package monitoring;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.dto.EmailNotificationData;
import monitoring.dto.JumpPulse;
import monitoring.service.EmailDataProvider;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class JumpsEmailNotifierApp {
	
	@Autowired
	JavaMailSender mailSender;
	final EmailDataProvider dataProvider;
	@Value("${app.email.service.address:hospitalservice@hospital.com}")
	private String hospitalServiceMail;
	@Value("${app.email.service.name:Hospital Alert Service}")
	private String hospitalServiceName;
	@Value("${app.email.subject:Pulse Jump }")
	private String subject;

	public static void main(String[] args) {
		SpringApplication.run(JumpsEmailNotifierApp.class, args);

	}
	
	@Bean
	Consumer<JumpPulse> jumpsConsumer() {
		return this::jumpProcessing;
	}
	void jumpProcessing(JumpPulse jumpPulse) {
		log.trace("received: {}", jumpPulse);
		sendMail(jumpPulse);
	}
	private void sendMail(JumpPulse jumpPulse) {
	
		EmailNotificationData data = dataProvider.getData(jumpPulse.patientId());
		if (data == null) {
			log.warn("email data have not been received");
			data = new EmailNotificationData(hospitalServiceMail, hospitalServiceName, "" +
		jumpPulse.patientId());
		}
		log.trace("email data: doctor email: {}, doctor name: {}, patient name: {}", data.doctorMail(), data.doctorName(), data.patientName());
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setTo(data.doctorMail());
		smm.setSubject(subject + jumpPulse.patientId());
		String text = getText(jumpPulse, data);
		smm.setText(text);
		mailSender.send(smm);
		log.trace("sent: {}", text);
		
	}
	private String getText(JumpPulse jumpPulse, EmailNotificationData data) {
		
		return String.format("Dear %s\nYour patient %s has the pulse jump\n"
				+ "previous value: %d\n"
				+ "current value: %d\n", data.doctorName(), data.patientName(),
				jumpPulse.prevValue(), jumpPulse.currentValue());
	}

}
