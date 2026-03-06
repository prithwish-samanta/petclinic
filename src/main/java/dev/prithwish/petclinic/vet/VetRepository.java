package dev.prithwish.petclinic.vet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VetRepository extends JpaRepository<Vet, Integer> {
}
