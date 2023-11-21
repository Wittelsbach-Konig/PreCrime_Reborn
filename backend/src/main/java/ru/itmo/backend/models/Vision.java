package ru.itmo.backend.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "visions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String videoUrl;
    @Builder.Default
    private boolean accepted = false; //поле техника что он просмотрел
}
