package ru.itmo.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "arrest_assignments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CriminalToReactGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "criminal_id", nullable = false)
    private Criminal criminal;
    @ManyToOne
    @JoinColumn(name = "react_group_id", nullable = false)
    private ReactGroup reactGroup;
}
