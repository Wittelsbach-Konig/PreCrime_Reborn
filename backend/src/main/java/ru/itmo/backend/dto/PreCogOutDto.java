package ru.itmo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreCogOutDto {
    private Long id;
    private String preCogName;
    private int age;
    private int dopamineLevel;
    private int serotoninLevel;
    private int stressLevel;
    private boolean isWork;
    private String commissionedOn;
}
