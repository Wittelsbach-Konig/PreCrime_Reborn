package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.ResourceDto;
import ru.itmo.precrimeupd.dto.TransportDto;
import ru.itmo.precrimeupd.model.GroupResource;
import ru.itmo.precrimeupd.model.Transport;

import java.util.List;
import java.util.NoSuchElementException;

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
