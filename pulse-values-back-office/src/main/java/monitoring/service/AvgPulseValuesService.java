package monitoring.service;

import java.time.LocalDateTime;
import java.util.List;

public interface AvgPulseValuesService {
	int getAvgValue(long patientId, LocalDateTime from, LocalDateTime to);
	int getMaxValue(long patientId, LocalDateTime from, LocalDateTime to);
	int getMinValue(long patientId, LocalDateTime from, LocalDateTime to);
	List<Integer> getAllValues(long patientId, LocalDateTime from, LocalDateTime to);
}
