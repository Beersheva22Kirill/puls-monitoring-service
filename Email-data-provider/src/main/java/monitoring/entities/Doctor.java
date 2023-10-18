package monitoring.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Doctor extends Person {
	
	public Doctor(long id, String email, String name) {
		super(id, email, name);
	}

	
}
