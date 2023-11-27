package ru.itmo.backend.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @NotNull
    @NotEmpty
    @Size(max = 75)
    private String memberName;

    @Column(unique = true)
    private int telegramId;
}
