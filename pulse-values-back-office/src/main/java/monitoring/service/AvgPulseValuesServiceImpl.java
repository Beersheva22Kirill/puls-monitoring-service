package monitoring.service;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import static monitoring.documents.AvgPulseDoc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.documents.AvgPulseDoc;
import monitoring.repositoies.AvgPulseRepo;

@RequiredArgsConstructor
@Service
@Slf4j
public class AvgPulseValuesServiceImpl implements AvgPulseValuesService {
	
    final MongoTemplate mongoTemplate;
    final AvgPulseRepo avgPulseRepo;
    
    private static final String AVG_VALUE = "avgValue";
	private static final String MIN_VALUE = "minValue";
	private static final String MAX_VALUE = "maxValue";
	@Override
	public int getAvgValue(long patientId, LocalDateTime from, LocalDateTime to) {

		
		return avgValueRequest(patientId, from, to);
	}

	@Override
	public int getMaxValue(long patientId, LocalDateTime from, LocalDateTime to) {
		
		return maxValueRequest(patientId, from, to);
	}

	@Override
	public int getMinValue(long patientId, LocalDateTime from, LocalDateTime to) {
		return minValueRequest(patientId, from, to);
	}

	@Override
	public List<Integer> getAllValues(long patientId, LocalDateTime from, LocalDateTime to) {
		List<AvgPulseDoc> documents = avgPulseRepo.findByPatientIdAndDateTimeBetween(patientId, from, to);
		return documents.stream().map(AvgPulseDoc::getValue).toList();
	}
private int avgValueRequest(long patientId,LocalDateTime from, LocalDateTime to ) {
	
		String fieldName = AVG_VALUE;
		
		GroupOperation groupOperation = group().avg(PULSE_VALUE).as(fieldName);
		Document document = getAggregationDocument( groupOperation, patientId, from, to);
		return document == null ? 0 : document.getDouble(fieldName).intValue();
	}
private int minValueRequest(long patientId, LocalDateTime from, LocalDateTime to) {
	
	String fieldName = MIN_VALUE;
	
	GroupOperation groupOperation = group().min(PULSE_VALUE).as(fieldName);
	Document document = getAggregationDocument( groupOperation, patientId, from, to);
	return document == null ? 0 : document.getInteger(fieldName);
}
private int maxValueRequest(long patientId, LocalDateTime from, LocalDateTime to) {
	
	String fieldName = MAX_VALUE;
	
	GroupOperation groupOperation = group().max(PULSE_VALUE).as(fieldName);
	Document document = getAggregationDocument( groupOperation, patientId, from, to);
	return document == null ? 0 : document.getInteger(fieldName);
}

private Document getAggregationDocument(GroupOperation groupOperation, long patientId, LocalDateTime from, LocalDateTime to) {
	MatchOperation matchOperation = match(Criteria.where(PATIENT_ID).is(patientId)
			.andOperator(Criteria.where(DATE_TIME).gte(from).lte(to)));
	Aggregation pipeline = newAggregation(matchOperation, groupOperation);
	Document document = mongoTemplate.aggregate(pipeline, AvgPulseDoc.class, Document.class)
			.getUniqueMappedResult();
	return document;
}

}
