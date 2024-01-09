package ru.itmo.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itmo.backend.dto.ReactGroupInDto;
import ru.itmo.backend.dto.ReactGroupOutDto;
import ru.itmo.backend.exceptions.NotFoundException;
import ru.itmo.backend.models.Criminal;
import ru.itmo.backend.models.CriminalToReactGroup;
import ru.itmo.backend.models.ReactGroup;
import ru.itmo.backend.models.UserEntity;
import ru.itmo.backend.repository.CriminalRepository;
import ru.itmo.backend.repository.CriminalToReactGroupRepository;
import ru.itmo.backend.repository.ReactGroupRepository;
import ru.itmo.backend.repository.UserRepository;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.ReactGroupService;
import ru.itmo.backend.service.StatisticService;
import ru.itmo.backend.service.TelegramBotService;
//import ru.itmo.precrimesyst.service.TelegramBotService;

import java.util.List;

import static ru.itmo.backend.mapper.ReactGroupMapper.mapToReactGroup;
import static ru.itmo.backend.mapper.ReactGroupMapper.mapToReactGroupOutDto;

@Service
public class ReactGroupServiceImpl implements ReactGroupService {
    private final ReactGroupRepository reactGroupRepository;
    private final TelegramBotService telegramBotService;
    private final CriminalRepository criminalRepository;
    private final StatisticService statisticService;
    private final UserRepository userRepository;
    private final CriminalToReactGroupRepository criminalToReactGroupRepository;

    private SecurityUtil securityUtil;

    @Autowired
    public ReactGroupServiceImpl(ReactGroupRepository reactGroupRepository
                                , TelegramBotService telegramBotService
                                , CriminalRepository criminalRepository
                                , StatisticService statisticService
                                , UserRepository userRepository
                                , CriminalToReactGroupRepository criminalToReactGroupRepository

                                , SecurityUtil securityUtil) {
        this.reactGroupRepository = reactGroupRepository;
        this.telegramBotService = telegramBotService;
        this.criminalRepository = criminalRepository;
        this.statisticService = statisticService;
        this.userRepository = userRepository;
        this.criminalToReactGroupRepository = criminalToReactGroupRepository;

        this.securityUtil = securityUtil;
    }

    @Override
    public ReactGroupOutDto createNewGroupMember(ReactGroupInDto reactGroupInDto) {
        ReactGroup reactGroup = mapToReactGroup(reactGroupInDto);
        ReactGroup savedGroup = reactGroupRepository.save(reactGroup);
        statisticService.createNewStatisticRecordReactGroup(reactGroup.getTelegramId());
        return mapToReactGroupOutDto(savedGroup);
    }

    @Override
    public ReactGroup findGroupMemberById(Long id) {
        return reactGroupRepository.findById(id).orElseThrow(()-> new NotFoundException("Member not found: " + id));
    }

    @Override
    public Criminal findCriminalById(Long id) {
        return criminalRepository.findById(id).orElseThrow(()-> new NotFoundException("Criminal not found: " + id));
    }

    @Override
    public List<ReactGroup> getAllMembers() {
        List<ReactGroup> reactGroups = reactGroupRepository.findAll(Sort.by(Sort.Direction.DESC, "inOperation"));
        return reactGroups;
    }

    @Override
    public List<ReactGroup> getAllWorkingMembers() {
        List<ReactGroup> workingReactGroups = reactGroupRepository.findAllByInOperationIsTrue();
        return workingReactGroups;
    }

    @Override
    public ReactGroupOutDto retireGroupMember(Long id) {
        ReactGroup memberToRetire = findGroupMemberById(id);
        memberToRetire.setInOperation(false);
        ReactGroup retiredMember = reactGroupRepository.save(memberToRetire);
        return mapToReactGroupOutDto(retiredMember);
    }

//    @Override
//    public void deleteGroupMember(Long id) {
//        ReactGroup reactGroupMember = findGroupMemberById(id);
//        reactGroupRepository.delete(reactGroupMember);
//    }

    @Override
    public ReactGroupOutDto updateGroupMember(Long id, ReactGroupInDto reactGroupInDto) {
        ReactGroup groupToUpdate = findGroupMemberById(id);
        groupToUpdate.setMemberName(reactGroupInDto.getMemberName());
        groupToUpdate.setTelegramId(reactGroupInDto.getTelegramId());
        ReactGroup updatedMember = reactGroupRepository.save(groupToUpdate);
        return mapToReactGroupOutDto(updatedMember);
    }

    @Override
    public void appointGroupToCriminal(Long id, List<Long> group) {
        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        statisticService.appointedToArrest(user);

        Criminal criminal = findCriminalById(id);
        criminal.setArrestAssigned(true);
        String message = "You have been assigned to arrest a criminal, information:\n"
                + "Criminal Name: " + criminal.getName() + "\n"
                + "Last known location: " + criminal.getLocation()
                + "Weapon :" + criminal.getWeapon();
        for(Long memberId : group){
            ReactGroup groupMember = findGroupMemberById(memberId);
            if (!groupMember.isInOperation()) {
                groupMember.setInOperation(true);
            }
            int chatId = groupMember.getTelegramId();
            telegramBotService.sendMessage(chatId, message);
            CriminalToReactGroup arrestAssignment = new CriminalToReactGroup();
            arrestAssignment.setCriminal(criminal);
            arrestAssignment.setReactGroup(groupMember);
            criminalToReactGroupRepository.save(arrestAssignment);
        }
        criminalRepository.save(criminal);
    }

    @Override
    public List<ReactGroupOutDto> getMembersAssignedToArrestCriminal(Long id) {
        Criminal criminal = findCriminalById(id);
        List<CriminalToReactGroup> criminalToReactGroups = criminalToReactGroupRepository.findAllByCriminal(criminal);
        List<ReactGroupOutDto> assignedMembers = null;
        for(CriminalToReactGroup record : criminalToReactGroups) {
            assignedMembers.add(mapToReactGroupOutDto(record.getReactGroup()));
        }
        return assignedMembers;
    }
}
