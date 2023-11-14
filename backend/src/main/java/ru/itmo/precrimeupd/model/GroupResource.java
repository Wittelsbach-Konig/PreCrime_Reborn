package ru.itmo.precrimeupd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
