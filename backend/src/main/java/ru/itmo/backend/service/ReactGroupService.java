package ru.itmo.backend.service;

import ru.itmo.backend.dto.ReactGroupDto;
import ru.itmo.backend.models.Criminal;
import ru.itmo.backend.models.ReactGroup;

import java.util.List;

public interface ReactGroupService {
    ReactGroupDto createNewGroupMember(ReactGroupDto reactGroupDto);
    ReactGroup findGroupMemberById(Long id);
    Criminal findCriminalById(Long id);
    List<ReactGroup> getAllMembers();
    void deleteGroupMember(Long id);
    ReactGroupDto updateGroupMember(Long id, ReactGroupDto reactGroupDto);
    void appointGroupToCriminal(Long id, List<Long> group);
}
