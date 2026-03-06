package dev.prithwish.petclinic.vet;

import dev.prithwish.petclinic.model.NamedEntity;
import dev.prithwish.petclinic.model.Person;
import jakarta.persistence.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vets")
public class Vet extends Person {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "vet_specialties",
            joinColumns = @JoinColumn(name = "vet_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Specialty> specialties;

    private Set<Specialty> getSpecialtiesInternal() {
        if (specialties == null) {
            specialties = new HashSet<>();
        }
        return specialties;
    }

    public List<Specialty> getSpecialties() {
        return getSpecialtiesInternal().stream()
                .sorted(Comparator.comparing(NamedEntity::getName))
                .toList();
    }

    public int getNrOfSpecialities() {
        return getSpecialtiesInternal().size();
    }

    public void addSpecialty(Specialty specialty) {
        getSpecialtiesInternal().add(specialty);
    }
}
