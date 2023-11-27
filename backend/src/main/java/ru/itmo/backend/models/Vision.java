package ru.itmo.backend.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "visions", uniqueConstraints = @UniqueConstraint(columnNames = "videoUrl"))
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String videoUrl;

    @Builder.Default
    private boolean accepted = false; //поле техника что он просмотрел
}
