package ru.itmo.backend.service;

import ru.itmo.backend.dto.ReactGroupInDto;
import ru.itmo.backend.dto.ReactGroupOutDto;
import ru.itmo.backend.models.Criminal;
import ru.itmo.backend.models.ReactGroup;

import java.util.List;

public interface ReactGroupService {
    ReactGroupOutDto createNewGroupMember(ReactGroupInDto reactGroupInDto);
    ReactGroup findGroupMemberById(Long id);
    Criminal findCriminalById(Long id);
    List<ReactGroup> getAllMembers();
    List<ReactGroup> getAllWorkingMembers();
    //void deleteGroupMember(Long id);
    ReactGroupOutDto retireGroupMember(Long id);
    ReactGroupOutDto updateGroupMember(Long id, ReactGroupInDto reactGroupInDto);
    void appointGroupToCriminal(Long id, List<Long> group);
}
