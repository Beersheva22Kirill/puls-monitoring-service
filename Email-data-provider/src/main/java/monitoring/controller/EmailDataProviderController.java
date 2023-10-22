package monitoring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import monitoring.dto.EmailNotificationData;
import monitoring.service.EmailDataProvider;
import monitoring.service.RandomDataBaseService;

@RestController
@RequiredArgsConstructor
public class EmailDataProviderController {
	final RandomDataBaseService randomDatabase;
	final EmailDataProvider service;
	
	@GetMapping("data/{patientId}")
	ResponseEntity<?> getData(@PathVariable long patientId) {
		ResponseEntity<?> res = null;
		try {
			EmailNotificationData data = service.getData(patientId);
			res =  new ResponseEntity<EmailNotificationData>(data, HttpStatus.OK);
		} catch (Exception e) {
			res = new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		return res;
		
		
	}
	
	@PostConstruct
	void createRandomDataBase(){
		randomDatabase.createRandomDataBase();
	}

}
