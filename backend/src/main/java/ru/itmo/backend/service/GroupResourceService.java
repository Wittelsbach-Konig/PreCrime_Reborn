package ru.itmo.backend.service;

import ru.itmo.backend.dto.ResourceDto;
import ru.itmo.backend.dto.TransportDto;
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
    TransportDto addNewTransport(TransportDto transportDto);
    void retireTransport(Long id);
    void rehabilitateTransport(Long id);
    void deleteTransport(Long id);
}
