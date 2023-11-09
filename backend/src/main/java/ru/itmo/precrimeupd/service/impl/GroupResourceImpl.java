package ru.itmo.precrimeupd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.ResourceDto;
import ru.itmo.precrimeupd.model.GroupResource;
import ru.itmo.precrimeupd.model.GroupResourceType;
import ru.itmo.precrimeupd.repository.GroupResourceRepository;
import ru.itmo.precrimeupd.service.GroupResourceService;

import java.util.*;

@Service
public class GroupResourceImpl implements GroupResourceService {
    private GroupResourceRepository groupResourceRepository;

    @Autowired
    public GroupResourceImpl(GroupResourceRepository groupResourceRepository) {
        this.groupResourceRepository = groupResourceRepository;
    }

    @Override
    public List<GroupResource> getAllResources() {
        return groupResourceRepository.findAll();
    }

    @Override
    public GroupResource findResourceById(Long id) {
        Optional<GroupResource> resource = groupResourceRepository.findById(id);
        if(resource.isPresent()) {
            return resource.get();
        }
        return null;
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
                    resources.addAll(groupResourceRepository.findByType(GroupResourceType.AMMUNITION));
                    break;
                case "WEAPON":
                    resources.addAll(groupResourceRepository.findByType(GroupResourceType.WEAPON));
                    break;
                case "GADGET":
                    resources.addAll(groupResourceRepository.findByType(GroupResourceType.GADGET));
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
        GroupResource resourceToOrder = groupResourceRepository.findById(id).get();
        int maxPossibleAmount = resourceToOrder.getMaxPossibleAmount();
        int currentAmount = resourceToOrder.getAmount();
        int newAmount = currentAmount + amount;
        if(newAmount > maxPossibleAmount){
            throw new IllegalArgumentException("Trying to order too much resources");
        }
        resourceToOrder.setAmount(newAmount);
        //else throw exception
        groupResourceRepository.save(resourceToOrder);
    }
}
