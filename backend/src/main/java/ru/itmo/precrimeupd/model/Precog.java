package ru.itmo.precrimeupd.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "precogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Precog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String precogName;
    private int age;
    private int dopamineLevel;
    private int serotoninLevel;
    private int stressLevel;
    @Builder.Default
    private boolean isOk = true;
    // can be added date of commissioning
    @CreationTimestamp
    private LocalDateTime commissionedOn;
}
