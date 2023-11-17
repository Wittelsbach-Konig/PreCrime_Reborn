package ru.itmo.precrimeupd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.ResourceDto;
import ru.itmo.precrimeupd.dto.TransportDto;
import ru.itmo.precrimeupd.exceptions.NotFoundException;
import ru.itmo.precrimeupd.model.GroupResource;
import ru.itmo.precrimeupd.model.GroupResourceType;
import ru.itmo.precrimeupd.model.Transport;
import ru.itmo.precrimeupd.model.UserEntity;
import ru.itmo.precrimeupd.repository.GroupResourceRepository;
import ru.itmo.precrimeupd.repository.TransportRepository;
import ru.itmo.precrimeupd.repository.UserRepository;
import ru.itmo.precrimeupd.security.SecurityUtil;
import ru.itmo.precrimeupd.service.GroupResourceService;
import ru.itmo.precrimeupd.service.StatisticService;

import java.util.*;

@Service
public class GroupResourceImpl implements GroupResourceService {
    private final GroupResourceRepository groupResourceRepository;
    private final TransportRepository transportRepository;
    private final UserRepository userRepository;
    private final StatisticService statisticService;

    @Autowired
    public GroupResourceImpl(GroupResourceRepository groupResourceRepository
            , TransportRepository transportRepository
            , UserRepository userRepository
            , StatisticService statisticService) {
        this.groupResourceRepository = groupResourceRepository;
        this.transportRepository = transportRepository;
        this.userRepository = userRepository;
        this.statisticService = statisticService;
    }

    @Override
    public List<GroupResource> getAllResources() {
        return groupResourceRepository.findAll();
    }

    @Override
    public GroupResource findResourceById(Long id) {
        return groupResourceRepository.findById(id).orElseThrow(() -> new NotFoundException("Resource not found: " + id));
    }

    @Override
    public List<GroupResource> getResourcesByType(List<String> types) {
        if(types.isEmpty()) {
            return getAllResources();
        }
        Set<String> resourceTypes = new HashSet<>(types);
        List<GroupResource> resources = new ArrayList<>();
        for(String type : resourceTypes) {
            switch (type){
                case "AMMUNITION":
                    resources.addAll(groupResourceRepository.findAllByType(GroupResourceType.AMMUNITION));
                    break;
                case "WEAPON":
                    resources.addAll(groupResourceRepository.findAllByType(GroupResourceType.WEAPON));
                    break;
                case "GADGET":
                    resources.addAll(groupResourceRepository.findAllByType(GroupResourceType.GADGET));
                default:
                    break;
            }
        }
        return resources;
    }

    @Override
    public void addNewResource(ResourceDto resourceDto) throws IllegalArgumentException {
        GroupResource resource = groupResourceRepository.findByResourceName(resourceDto.getResourceName());
        if(resource != null && resource.getResourceName() != null && !resource.getResourceName().isEmpty()){
            throw new IllegalArgumentException("Such resource already exists");
        }
        GroupResource resourceToAdd = new GroupResource();
        resourceToAdd.setResourceName(resourceDto.getResourceName());
        resourceToAdd.setAmount(resourceDto.getAmount());
        resourceToAdd.setMaxPossibleAmount(resourceDto.getMaxPossibleAmount());
        String resourceType = resourceDto.getType();
        switch (resourceType) {
            case "AMMUNITION":
                resourceToAdd.setType(GroupResourceType.AMMUNITION);
                break;
            case "WEAPON":
                resourceToAdd.setType(GroupResourceType.WEAPON);
                break;
            case "GADGET":
                resourceToAdd.setType(GroupResourceType.GADGET);
                break;
            default:
                throw new IllegalArgumentException("Unknown resource type");
        }
        groupResourceRepository.save(resourceToAdd);
    }

    @Override
    public void orderResource(Long id, int amount) throws IllegalArgumentException {
        String login = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);

        GroupResource resourceToOrder = findResourceById(id);
        int maxPossibleAmount = resourceToOrder.getMaxPossibleAmount();
        int currentAmount = resourceToOrder.getAmount();
        int newAmount = currentAmount + amount;
        if(newAmount > maxPossibleAmount){
            throw new IllegalArgumentException("Trying to order too much resources");
        }
        resourceToOrder.setAmount(newAmount);
        groupResourceRepository.save(resourceToOrder);
        statisticService.orderedResource(user, amount, resourceToOrder.getType());
    }

    @Override
    public List<Transport> getAllTransport() {
        return transportRepository.findAll();
    }

    @Override
    public Transport findTransportById(Long id) {
        return transportRepository.findById(id).orElseThrow(() -> new NotFoundException("Transport not found: " + id));
    }

    @Override
    public void addNewTransport(TransportDto transportDto) {
        Transport transportToAdd = new Transport();
        transportToAdd.setBrand(transportDto.getBrand());
        transportToAdd.setModel(transportDto.getModel());
        transportToAdd.setMaximum_fuel(transportDto.getMaximum_fuel());
        transportToAdd.setRemaining_fuel(transportDto.getMaximum_fuel());
        transportRepository.save(transportToAdd);
    }

    @Override
    public void retireTransport(Long id) {
        Transport transportToRetire = findTransportById(id);
        transportToRetire.setInOperation(false);
        transportRepository.save(transportToRetire);

    }

    @Override
    public void rehabilitateTransport(Long id) {
        Transport transportToRehabilitate = findTransportById(id);
        transportToRehabilitate.setInOperation(true);
        transportRepository.save(transportToRehabilitate);
    }

    @Override
    public void deleteTransport(Long id) {
        Transport transport = findTransportById(id);
        transportRepository.delete(transport);
    }
}
