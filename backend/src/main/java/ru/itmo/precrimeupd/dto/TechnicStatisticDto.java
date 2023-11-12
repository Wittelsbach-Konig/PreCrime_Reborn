package ru.itmo.precrimeupd.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechnicStatisticDto {
    private Long id;
    private int visionsAccepted;
    private int visionsRejected;
    private int dopamineEntered;
    private int serotoninEntered;
    private int depressantEntered;
}
