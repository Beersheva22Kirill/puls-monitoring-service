package monitoring.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Patient extends Person{
	
		public Patient(long id, String email, String name) {
			super(id, email, name);
		}
}
