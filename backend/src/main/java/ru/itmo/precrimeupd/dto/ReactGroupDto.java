package ru.itmo.precrimeupd.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReactGroupDto {
    private Long id;
    @NotEmpty
    private String memberName;
    @NotEmpty
    private int telegramId;
}
