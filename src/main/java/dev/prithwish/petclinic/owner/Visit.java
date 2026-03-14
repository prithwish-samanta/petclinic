package dev.prithwish.petclinic.owner;

import dev.prithwish.petclinic.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "visits")
@Getter
@Setter
public class Visit extends BaseEntity {
    @Column(name = "visit_date")
    private LocalDate date;

    @NotBlank
    private String description;
}
