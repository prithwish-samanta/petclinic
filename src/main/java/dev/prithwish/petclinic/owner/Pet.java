package dev.prithwish.petclinic.owner;

import jakarta.persistence.*;
import dev.prithwish.petclinic.model.NamedEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "pets")
@Getter
@Setter
public class Pet extends NamedEntity {
    @Column
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PetType type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_id")
    private Set<Visit> visits;
}