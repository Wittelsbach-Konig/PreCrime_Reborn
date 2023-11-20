package ru.itmo.backend.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "react_groups")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String memberName;
    private int telegramId;
}
