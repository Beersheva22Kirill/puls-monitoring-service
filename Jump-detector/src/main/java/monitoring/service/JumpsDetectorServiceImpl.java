package monitoring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import monitoring.dto.JumpPulse;
import monitoring.dto.PulseProbe;
import monitoring.entity.LastPulsValue;
import monitoring.repo.LastPulsValueRepo;

@Service
@RequiredArgsConstructor
public class JumpsDetectorServiceImpl implements JumpsDetectorService {

	final LastPulsValueRepo LastPulsValueRepo;
	@Value("${app.jumps.threshold:0.3}")
	double jumpThreshold;
	
	@Override
	public JumpPulse processPulseProbe(PulseProbe pulse) {
		LastPulsValue lastPuls = LastPulsValueRepo.findById(pulse.patientId()).orElse(null);
		JumpPulse res = null;
		if (lastPuls != null && isJump(pulse.value(),lastPuls.getValue())) {
			res = new JumpPulse(pulse.patientId(), lastPuls.getValue(), pulse.value());
		}
		lastPuls = new LastPulsValue(pulse.patientId(),pulse.value());
		return res;
	}

	private boolean isJump(int currentValue, int prevValue) {
			int delta = Math.abs(currentValue - prevValue);
			
		return currentValue * jumpThreshold <= delta;
	}

}
