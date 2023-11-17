package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.ReactGroupDto;
import ru.itmo.precrimeupd.model.Criminal;
import ru.itmo.precrimeupd.model.ReactGroup;

import java.util.List;

public interface ReactGroupService {
    void createNewGroupMember(ReactGroupDto reactGroupDto);
    ReactGroup findGroupMemberById(Long id);
    List<ReactGroup> getAllMembers();
    void deleteGroupMember(Long id);
    Criminal findCriminalById(Long id);
    void updateGroupMember(Long id, ReactGroupDto reactGroupDto);
    void appointGroupToCriminal(Long id, List<Long> group);
}
