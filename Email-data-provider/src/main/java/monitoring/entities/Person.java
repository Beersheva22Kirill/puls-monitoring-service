package monitoring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "persons")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Person {
 @Id
 long id;
 String email;
 String name;
}
