package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.ReactGroupDto;
import ru.itmo.precrimeupd.model.ReactGroup;

import java.util.List;

public interface ReactGroupService {
    void createNewGroup(ReactGroupDto reactGroupDto);
    ReactGroup findGroupById(Long id);
    List<ReactGroup> getAllGroups();
    void deleteGroup(Long id);
    void updateGroup(Long id, ReactGroupDto reactGroupDto);
}
