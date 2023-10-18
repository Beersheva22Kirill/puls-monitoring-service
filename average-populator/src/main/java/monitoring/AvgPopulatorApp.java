package monitoring;

import java.util.function.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.documents.AvgPulseDoc;
import monitoring.dto.PulseProbe;
import monitoring.repo.AverageRepository;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AvgPopulatorApp {
	
	final AverageRepository avgPulseRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(AvgPopulatorApp.class, args);

	}
	
	@Bean
	Consumer<PulseProbe> avgPulseConsumer() {
		return this::getAvgPulseConsumer;
	}

	void getAvgPulseConsumer(PulseProbe pulseProbe) {
		log.trace("received pulseprobe of patient {}", pulseProbe.patientId());
		AvgPulseDoc pulseDoc = AvgPulseDoc.of(pulseProbe);
		avgPulseRepository.save(pulseDoc);
	}

}
