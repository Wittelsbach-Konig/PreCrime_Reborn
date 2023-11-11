package ru.itmo.precrimeupd.model;


import lombok.*;

import javax.persistence.*;

@Entity(name = "react_group_statistic")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReactGroupStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private ReactGroup member;
    @Builder.Default
    private int criminalsCaught = 0;
    @Builder.Default
    private int criminalsEscaped = 0;
}
