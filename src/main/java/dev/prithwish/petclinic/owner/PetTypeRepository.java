package dev.prithwish.petclinic.owner;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PetTypeRepository extends JpaRepository<PetType, Integer> {
    PetType findByName(String type);
}
