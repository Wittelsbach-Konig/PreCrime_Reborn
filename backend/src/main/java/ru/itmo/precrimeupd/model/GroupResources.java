package ru.itmo.precrimeupd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupResources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String resourceName;
    private int amount;
    // optional value, can be deleted
    private int maxPossibleAmount;
}
