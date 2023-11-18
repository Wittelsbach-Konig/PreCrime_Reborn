package ru.itmo.precrimeupd.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "crimecards")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrimeCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String victimName;
    private String criminalName;
    private String placeOfCrime;
    private String weapon;
    private LocalDateTime crimeTime;
    @Enumerated(EnumType.STRING)
    private CrimeType typeOfCrime;
    @ManyToOne
    @JoinColumn(name = "responsible_detective_id", nullable = true)
    private UserEntity responsibleDetective;
    @Builder.Default
    private Boolean isCriminalCaught = false;
    @OneToOne
    @JoinColumn(name = "vision_id", nullable = false)
    private Vision vision;
}
