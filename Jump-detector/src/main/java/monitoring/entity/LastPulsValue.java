package monitoring.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RedisHash
@Getter
@AllArgsConstructor
public class LastPulsValue {
	
	@Id
	long parentId;
	int value;
}
