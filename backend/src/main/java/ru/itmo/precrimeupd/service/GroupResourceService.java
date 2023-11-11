package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.ResourceDto;
import ru.itmo.precrimeupd.dto.TransportDto;
import ru.itmo.precrimeupd.model.GroupResource;
import ru.itmo.precrimeupd.model.Transport;

import java.util.List;

public interface GroupResourceService {
    List<GroupResource> getAllResources();
    GroupResource findResourceById(Long id);
    List<GroupResource> getResourcesByType(List<String> types);
    void addNewResource(ResourceDto resourceDto) throws IllegalArgumentException;
    void orderResource(Long id, int amount) throws IllegalArgumentException;
    List<Transport> getAllTransport();
    Transport findTransportById(Long id);
    void addNewTransport(TransportDto transportDto);
    void retireTransport(Long id);
    void rehabilitateTransport(Long id);
    void deleteTransport(Long id);
}
