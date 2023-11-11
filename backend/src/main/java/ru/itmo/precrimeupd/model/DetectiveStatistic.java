package ru.itmo.precrimeupd.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "detective_statistic")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetectiveStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "detective_id", nullable = false)
    private UserEntity detective;
    @Builder.Default
    private int investigationCount = 0;
    @Builder.Default
    private int errorsInCards = 0;
}
