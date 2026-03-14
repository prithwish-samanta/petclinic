package dev.prithwish.petclinic.owner;

import jakarta.persistence.*;
import dev.prithwish.petclinic.model.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "owners")
@Getter
@Setter
public class Owner extends Person {
    @Column
    @NotBlank
    private String address;

    @Column
    @NotBlank
    private String city;

    @Column
    @NotBlank
    @Pattern(regexp = "\\d{10}")
    private String telephone;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private List<Pet> pets;
}
