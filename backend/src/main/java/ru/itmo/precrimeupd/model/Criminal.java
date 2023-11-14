package ru.itmo.precrimeupd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "criminals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Criminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private String weapon;
    @Enumerated(EnumType.STRING)
    private CriminalStatus status;
    @OneToOne
    @JoinColumn(name = "crime_card_id", nullable = false)
    private CrimeCard crimeCard;
}
