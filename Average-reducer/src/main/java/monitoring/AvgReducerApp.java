package monitoring;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.dto.PulseProbe;
import monitoring.service.AvgReducerService;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AvgReducerApp {
	
	final AvgReducerService service;
	final StreamBridge streamBridge;
	@Value("${app.avg.binding.name:avg-reducer-out-0}")
	String bindingName;
	
	public static void main(String[] args) {
		SpringApplication.run(AvgReducerApp.class, args);

	}
	
	@Bean
	Consumer<PulseProbe> pulseProbeConsumer() {
		return this::processPulseProbe;
	}
	void processPulseProbe(PulseProbe probe) {
		long patientId = probe.patientId();
		Integer avgValue = service.reduce(probe);
		if (avgValue != null) {
			log.debug("for patient {} avg value is {}", patientId, avgValue);
			streamBridge.send(bindingName, new PulseProbe(patientId,avgValue, System.currentTimeMillis(), 0));
		} else {
			log.trace("for patient {} no avg value yet", patientId);
		}
		
	}

}
