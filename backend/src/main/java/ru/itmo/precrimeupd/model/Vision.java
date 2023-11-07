package ru.itmo.precrimeupd.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "visions")
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
