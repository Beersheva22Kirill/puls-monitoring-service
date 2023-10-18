package monitoring.service;

import monitoring.dto.PulseProbe;

public interface AvgReducerService {
	Integer reduce(PulseProbe probe);
}
