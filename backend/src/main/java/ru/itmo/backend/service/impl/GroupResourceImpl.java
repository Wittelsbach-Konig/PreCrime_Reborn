package ru.itmo.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itmo.backend.dto.ResourceDto;
import ru.itmo.backend.dto.TransportDto;
import ru.itmo.backend.dto.TransportOutDto;
import ru.itmo.backend.exceptions.NotFoundException;
import ru.itmo.backend.exceptions.NotValidArgumentException;
import ru.itmo.backend.models.GroupResource;
import ru.itmo.backend.models.GroupResourceType;
import ru.itmo.backend.models.Transport;
import ru.itmo.backend.models.UserEntity;
import ru.itmo.backend.repository.GroupResourceRepository;
import ru.itmo.backend.repository.TransportRepository;
import ru.itmo.backend.repository.UserRepository;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.GroupResourceService;
import ru.itmo.backend.service.StatisticService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.itmo.backend.mapper.TransportMapper.mapToTransportOutDto;

@Service
public class GroupResourceImpl implements GroupResourceService {
    private final GroupResourceRepository groupResourceRepository;
    private final TransportRepository transportRepository;
    private final UserRepository userRepository;
    private final StatisticService statisticService;
    private final SecurityUtil securityUtil;

    @Autowired
    public GroupResourceImpl(GroupResourceRepository groupResourceRepository
            , TransportRepository transportRepository
            , UserRepository userRepository
            , StatisticService statisticService

            , SecurityUtil securityUtil) {
        this.groupResourceRepository = groupResourceRepository;
        this.transportRepository = transportRepository;
        this.userRepository = userRepository;
        this.statisticService = statisticService;
        this.securityUtil = securityUtil;
    }

    @Override
    public List<GroupResource> getAllResources() {
        return groupResourceRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public GroupResource findResourceById(Long id) {
        return groupResourceRepository.findById(id).orElseThrow(()-> new NotFoundException("Resource not found:" + id));
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
                case "FUEL":
                    resources.addAll(groupResourceRepository.findAllByType(GroupResourceType.FUEL));
                default:
                    break;
            }
        }
        return resources;
    }

    @Override
    public ResourceDto addNewResource(ResourceDto resourceDto) {
        GroupResource resource = groupResourceRepository.findByResourceName(resourceDto.getResourceName());
        if(resource != null && resource.getResourceName() != null && !resource.getResourceName().isEmpty()){
            throw new NotValidArgumentException("Such resource already exists: " + resourceDto.getResourceName());
        }
        GroupResource resourceToAdd = new GroupResource();
        resourceToAdd.setResourceName(resourceDto.getResourceName());
        resourceToAdd.setAmount(resourceDto.getAmount());
        resourceToAdd.setMaxPossibleAmount(resourceDto.getMaxPossibleAmount());
        String resourceType = resourceDto.getType();
        switch (resourceType) {
            case "AMMUNITION" -> resourceToAdd.setType(GroupResourceType.AMMUNITION);
            case "WEAPON" -> resourceToAdd.setType(GroupResourceType.WEAPON);
            case "GADGET" -> resourceToAdd.setType(GroupResourceType.GADGET);
            case "FUEL" -> resourceToAdd.setType(GroupResourceType.FUEL);
            default -> throw new NotValidArgumentException("Unknown resource type: " + resourceType);
        }
        GroupResource newResource = groupResourceRepository.save(resourceToAdd);
        return ResourceDto.builder()
                .id(newResource.getId())
                .resourceName(newResource.getResourceName())
                .amount(newResource.getAmount())
                .maxPossibleAmount(newResource.getMaxPossibleAmount())
                .type(newResource.getType().name())
                .build();
    }

    @Override
    public void orderResource(Long id, int amount) {
        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);

        GroupResource resourceToOrder = findResourceById(id);
        int maxPossibleAmount = resourceToOrder.getMaxPossibleAmount();
        int currentAmount = resourceToOrder.getAmount();
        int newAmount = currentAmount + amount;
        if(newAmount > maxPossibleAmount){
            throw new NotValidArgumentException("Trying to order too much resources. Maximum value for this resource is: "
                    + maxPossibleAmount);
        }
        resourceToOrder.setAmount(newAmount);
        groupResourceRepository.save(resourceToOrder);
        statisticService.orderedResource(user, amount, resourceToOrder.getType());
    }

    @Override
    public void refuelCar(Long id, int amount) {
        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        Transport transportToRefuel = findTransportById(id);
        int maxPossibleAmount = transportToRefuel.getMaximum_fuel();
        int currentAmount = transportToRefuel.getRemaining_fuel();
        int newAmount = currentAmount + amount;
        if(newAmount > maxPossibleAmount){
            throw new NotValidArgumentException("Trying to refuel too much gasoline. Maximum possible fuel amount for car: "
                    + maxPossibleAmount);
        }
        transportToRefuel.setRemaining_fuel(newAmount);
        transportRepository.save(transportToRefuel);
        statisticService.carReFueled(user, amount);
    }

    @Override
    public List<Transport> getAllTransport() {
        return transportRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Transport findTransportById(Long id) {
        return transportRepository.findById(id).orElseThrow(()-> new NotFoundException("Transport not found:" + id));
    }

    @Override
    public TransportOutDto addNewTransport(TransportDto transportDto) {
        Transport transportToAdd = new Transport();
        transportToAdd.setBrand(transportDto.getBrand());
        transportToAdd.setModel(transportDto.getModel());
        transportToAdd.setMaximum_fuel(transportDto.getMaximum_fuel());
        transportToAdd.setRemaining_fuel(0);
        Transport newTransport = transportRepository.save(transportToAdd);
        return TransportOutDto.builder()
                .id(newTransport.getId())
                .brand(newTransport.getBrand())
                .model(newTransport.getModel())
                .maximum_fuel(newTransport.getMaximum_fuel())
                .current_fuel(newTransport.getRemaining_fuel())
                .status(newTransport.getInOperation())
                .condition(newTransport.getCondition())
                .build();
    }

    @Override
    public TransportOutDto retireTransport(Long id) {
        Transport transportToRetire = findTransportById(id);
        transportToRetire.setInOperation(false);
        Transport savedTransport = transportRepository.save(transportToRetire);
        return mapToTransportOutDto(savedTransport);
    }

    @Override
    public TransportOutDto rehabilitateTransport(Long id)  {
        Transport transportToRehabilitate = findTransportById(id);
        transportToRehabilitate.setInOperation(true);
        Transport savedTransport = transportRepository.save(transportToRehabilitate);
        return mapToTransportOutDto(savedTransport);
    }

    @Override
    public void deleteTransport(Long id) {
        Transport transportToDelete = findTransportById(id);
        transportRepository.delete(transportToDelete);
    }
}
