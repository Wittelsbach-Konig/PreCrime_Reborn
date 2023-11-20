package ru.itmo.backend.models;

import lombok.*;

import javax.persistence.*;

@Entity(name = "resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String resourceName;
    private int amount;
    private int maxPossibleAmount;
    @Enumerated(EnumType.STRING)
    private GroupResourceType type;
}
