package ru.itmo.backend.mapper;

import ru.itmo.backend.dto.TransportOutDto;
import ru.itmo.backend.models.Transport;

public class TransportMapper {
    public static TransportOutDto mapToTransportOutDto(Transport transport){
        return TransportOutDto.builder()
                .id(transport.getId())
                .brand(transport.getBrand())
                .model(transport.getModel())
                .status(transport.getInOperation())
                .condition(transport.getCondition())
                .current_fuel(transport.getRemaining_fuel())
                .maximum_fuel(transport.getMaximum_fuel())
                .build();
    }
}
