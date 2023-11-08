package ru.itmo.precrimeupd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.ReactGroupDto;
import ru.itmo.precrimeupd.model.ReactGroup;
import ru.itmo.precrimeupd.repository.ReactGroupRepository;
import ru.itmo.precrimeupd.service.ReactGroupService;

import java.util.List;
import java.util.Optional;

import static ru.itmo.precrimeupd.mapper.ReactGroupMapper.mapToReactGroup;

@Service
public class ReactGroupServiceImpl implements ReactGroupService {
    private ReactGroupRepository reactGroupRepository;

    @Autowired
    public ReactGroupServiceImpl(ReactGroupRepository reactGroupRepository) {
        this.reactGroupRepository = reactGroupRepository;
    }

    @Override
    public void createNewGroup(ReactGroupDto reactGroupDto) {
        ReactGroup reactGroup = mapToReactGroup(reactGroupDto);
        reactGroupRepository.save(reactGroup);
    }

    @Override
    public ReactGroup findGroupById(Long id) {
        Optional<ReactGroup> reactGroup = reactGroupRepository.findById(id);
        return reactGroup.orElse(null);
    }

    @Override
    public List<ReactGroup> getAllGroups() {
        List<ReactGroup> reactGroups = reactGroupRepository.findAll();
        return reactGroups;
    }

    @Override
    public void deleteGroup(Long id) {
        Optional<ReactGroup> reactGroup = reactGroupRepository.findById(id);
        if(reactGroup.isPresent()){
            reactGroupRepository.deleteById(id);
        }
    }

    @Override
    public void updateGroup(Long id, ReactGroupDto reactGroupDto) {
        ReactGroup groupToUpdate = reactGroupRepository.findById(id).get();
        groupToUpdate.setDriverName(reactGroupDto.getDriverName());
        groupToUpdate.setNegotiatorName(reactGroupDto.getNegotiatorName());
        groupToUpdate.setPointManName(reactGroupDto.getPointManName());
        groupToUpdate.setSniperName(reactGroupDto.getSniperName());
        groupToUpdate.setTelegramId(reactGroupDto.getTelegramId());
        reactGroupRepository.save(groupToUpdate);
    }

}
