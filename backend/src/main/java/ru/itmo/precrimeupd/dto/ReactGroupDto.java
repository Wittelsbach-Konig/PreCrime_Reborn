package ru.itmo.precrimeupd.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReactGroupDto {
    private Long id;
    @NotEmpty
    private String driverName;
    @NotEmpty
    private String negotiatorName;
    @NotEmpty
    private String pointManName;
    @NotEmpty
    private String sniperName;
    @NotEmpty
    private int telegramId;
}
