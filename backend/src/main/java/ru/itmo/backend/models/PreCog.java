package ru.itmo.backend.models;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "precogs")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreCog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String preCogName;
    private int age;
    @Builder.Default
    private int dopamineLevel = 100;
    @Builder.Default
    private int serotoninLevel = 100;
    @Builder.Default
    private int stressLevel = 0;
    @Builder.Default
    private boolean isWork = true;
    @CreationTimestamp
    private LocalDateTime commissionedOn;
}
