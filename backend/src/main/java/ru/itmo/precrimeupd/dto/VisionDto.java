package ru.itmo.precrimeupd.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class VisionDto {
    private Long id;
    @NotEmpty
    String videoUrl;
}
