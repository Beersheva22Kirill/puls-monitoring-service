package monitoring.repositoies;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import monitoring.documents.AvgPulseDoc;

public interface AvgPulseRepo extends MongoRepository<AvgPulseDoc, ObjectId> {
List<AvgPulseDoc> findByPatientIdAndDateTimeBetween(long patientId, LocalDateTime from, LocalDateTime to);
}
