package ru.itmo.backend.models;


import lombok.*;

import javax.persistence.*;

@Entity(name = "technic_statistic")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TechnicStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "technic_id", unique = true, nullable = false)
    private UserEntity technic;

    @Builder.Default
    private int visionsAccepted = 0;

    @Builder.Default
    private int visionsRejected = 0;

    @Builder.Default
    private int dopamineEntered = 0;

    @Builder.Default
    private int serotoninEntered = 0;

    @Builder.Default
    private int depressantEntered = 0;
}
