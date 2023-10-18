package monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Pattern;
import monitoring.service.AvgPulseValuesService;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value="/pulse/values")
@Validated
public class AvgPulseController {
	private static final String ISO_DATE_TIME = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}";
	@Autowired
	AvgPulseValuesService service;
	@GetMapping("avg/{id}")
	Integer getAvgValue(@PathVariable ("id") long patientId,
			@RequestParam (name = "from", required = false)
	@Pattern(regexp = ISO_DATE_TIME, message="wrong date/time format 'from'")String fromDateTime,
			@RequestParam(name = "to", required=false)
	@Pattern(regexp = ISO_DATE_TIME, message="wrong date/time format 'to'")String toDateTime) {
		
		
		LocalDateTime from = fromDateTime == null ? LocalDateTime.of(1000, 1, 1, 0, 0) : LocalDateTime.parse(fromDateTime);
		LocalDateTime to = toDateTime == null ? LocalDateTime.of(10000, 1, 1, 0, 0) : LocalDateTime.parse(toDateTime);
		
		return service.getAvgValue(patientId, from, to);
		
	
	}
	@GetMapping("min/{id}")
	Integer getMinValue(@PathVariable ("id") long patientId,
			@RequestParam (name = "from", required = false)
	@Pattern(regexp = ISO_DATE_TIME, message="wrong date/time format 'from'")String fromDateTime,
			@RequestParam(name = "to", required=false)
	@Pattern(regexp = ISO_DATE_TIME, message="wrong date/time format 'to'")String toDateTime) {
		
		
		LocalDateTime from = fromDateTime == null ? LocalDateTime.of(1000, 1, 1, 0, 0) : LocalDateTime.parse(fromDateTime);
		LocalDateTime to = toDateTime == null ? LocalDateTime.of(10000, 1, 1, 0, 0) : LocalDateTime.parse(toDateTime);
		
		return service.getMinValue(patientId, from, to);
		
		
	}
	@GetMapping("max/{id}")
	Integer getMaxValue(@PathVariable ("id") long patientId,
			@RequestParam (name = "from", required = false)
	@Pattern(regexp = ISO_DATE_TIME, message="wrong date/time format 'from'")String fromDateTime,
			@RequestParam(name = "to", required=false)
	@Pattern(regexp = ISO_DATE_TIME, message="wrong date/time format 'to'")String toDateTime) {
		
		
		LocalDateTime from = fromDateTime == null ? LocalDateTime.of(1000, 1, 1, 0, 0) : LocalDateTime.parse(fromDateTime);
		LocalDateTime to = toDateTime == null ? LocalDateTime.of(10000, 1, 1, 0, 0) : LocalDateTime.parse(toDateTime);
		
		return service.getMaxValue(patientId, from, to);
		
		
	}
	@GetMapping("{id}")
	List<Integer> getAllValue(@PathVariable ("id") long patientId,
			@RequestParam (name = "from", required = false)
	@Pattern(regexp = ISO_DATE_TIME, message="wrong date/time format 'from'")String fromDateTime,
			@RequestParam(name = "to", required=false)
	@Pattern(regexp = ISO_DATE_TIME, message="wrong date/time format 'to'")String toDateTime) {
		
		
		LocalDateTime from = fromDateTime == null ? LocalDateTime.of(1000, 1, 1, 0, 0) : LocalDateTime.parse(fromDateTime);
		LocalDateTime to = toDateTime == null ? LocalDateTime.of(10000, 1, 1, 0, 0) : LocalDateTime.parse(toDateTime);
		
		return service.getAllValues(patientId, from, to);
		
		
	}
}