package monitoring;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.dto.JumpPulse;
import monitoring.dto.PulseProbe;
import monitoring.service.JumpsDetectorService;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class JampDetectorApp {
	
	final StreamBridge streamBridge;
	final JumpsDetectorService jumpsDetectorService;
	@Value("${app.jumps.binding.name:jumps-out-0}")
	String jumpsBindingName;
	
	public static void main(String[] args) {
		SpringApplication.run(JampDetectorApp.class, args);

	}
	@Bean
	Consumer<PulseProbe> pulseProbeConsumer(){
		
		return pulseProbe -> probeConsumer(pulseProbe);
	}
	
	void probeConsumer(PulseProbe pulseProbe) {
		JumpPulse jump = jumpsDetectorService.processPulseProbe(pulseProbe);
		if (jump != null) {
			streamBridge.send(jumpsBindingName, jump);	
			log.debug("jump {} sent to {}", jump, jumpsBindingName);
		} else {
			log.trace("No jump sent");
		}
	}

}
