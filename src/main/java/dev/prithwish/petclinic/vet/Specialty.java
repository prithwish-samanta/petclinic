package dev.prithwish.petclinic.vet;

import dev.prithwish.petclinic.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "specialties")
public class Specialty extends NamedEntity {

}
