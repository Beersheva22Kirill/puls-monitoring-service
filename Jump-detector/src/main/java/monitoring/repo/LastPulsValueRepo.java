package monitoring.repo;

import org.springframework.data.repository.CrudRepository;

import monitoring.entity.LastPulsValue;

public interface LastPulsValueRepo extends CrudRepository<LastPulsValue, Long>{
	
	

}
