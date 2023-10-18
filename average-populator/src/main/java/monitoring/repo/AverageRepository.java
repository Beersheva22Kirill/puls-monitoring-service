package monitoring.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import monitoring.documents.AvgPulseDoc;

public interface AverageRepository extends MongoRepository<AvgPulseDoc, ObjectId> {

}
