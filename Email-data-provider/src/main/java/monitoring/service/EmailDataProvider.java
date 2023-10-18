package monitoring.service;

import org.springframework.http.ResponseEntity;

import monitoring.dto.EmailNotificationData;
import monitoring.dto.ResponseData;

public interface EmailDataProvider {
	
	EmailNotificationData getData(long pacientId);
	
}
