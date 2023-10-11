package monitoring.service;

import monitoring.dto.JumpPulse;
import monitoring.dto.PulseProbe;

public interface JumpsDetectorService {
	
	JumpPulse processPulseProbe(PulseProbe pulse);

}
