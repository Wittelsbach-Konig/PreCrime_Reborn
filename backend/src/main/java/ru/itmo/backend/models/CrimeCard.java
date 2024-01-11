package ru.itmo.backend.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotEmpty
    @NotNull
    @Size(max = 75)
    private String victimName;

    @NotEmpty
    @NotNull
    @Size(max = 75)
    private String criminalName;

    @NotEmpty
    @NotNull
    @Size(max = 75)
    private String placeOfCrime;

    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String weapon;

    private LocalDateTime crimeTime;

    @Enumerated(EnumType.STRING)
    private CrimeType typeOfCrime;

    @ManyToOne
    @JoinColumn(name = "responsible_detective_id", nullable = false)
    private UserEntity responsibleDetective;

    @Builder.Default
    private Boolean isCriminalCaught = false;

    @OneToOne
    @Valid
    @JoinColumn(name = "vision_id", unique = true, nullable = false)
    private Vision vision;
    @CreationTimestamp
    private LocalDateTime creationDate;
}
