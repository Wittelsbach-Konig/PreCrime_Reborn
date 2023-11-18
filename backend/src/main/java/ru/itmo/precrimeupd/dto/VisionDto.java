package ru.itmo.precrimeupd.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class VisionDto {
    private Long id;
    @NotEmpty
    String videoUrl;
}
