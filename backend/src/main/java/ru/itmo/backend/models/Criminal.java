package ru.itmo.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "criminals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Criminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 75)
    private String name;

    @NotNull
    @NotEmpty
    @Size(max = 75)
    private String location;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String weapon;

    @Enumerated(EnumType.STRING)
    private CriminalStatus status;

    @OneToOne
    @JoinColumn(name = "crime_card_id",nullable = false)
    private CrimeCard crimeCard;
}
