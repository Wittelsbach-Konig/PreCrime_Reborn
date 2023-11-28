package ru.itmo.backend.service;

import ru.itmo.backend.dto.ResourceDto;
import ru.itmo.backend.dto.TransportDto;
import ru.itmo.backend.dto.TransportOutDto;
import ru.itmo.backend.models.GroupResource;
import ru.itmo.backend.models.Transport;

import java.util.List;

public interface GroupResourceService {
    List<GroupResource> getAllResources();
    GroupResource findResourceById(Long id);
    List<GroupResource> getResourcesByType(List<String> types);
    ResourceDto addNewResource(ResourceDto resourceDto);
    void orderResource(Long id, int amount);
    void refuelCar(Long id, int amount);
    List<Transport> getAllTransport();
    Transport findTransportById(Long id);
    TransportOutDto addNewTransport(TransportDto transportDto);
    TransportOutDto retireTransport(Long id);
    TransportOutDto rehabilitateTransport(Long id);
    void deleteTransport(Long id);
}
