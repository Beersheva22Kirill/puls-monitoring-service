package monitoring.repo;

import org.springframework.data.repository.CrudRepository;

import monitoring.entity.LastPulsValue;

public interface LastPulseValueRepo extends CrudRepository<LastPulsValue, Long>{
	
}
