package monitoring.service;

import monitoring.dto.EmailNotificationData;

public interface EmailDataProvider {
	EmailNotificationData getData(long patientId);
}
