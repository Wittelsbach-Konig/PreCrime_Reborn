package ru.itmo.precrimeupd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.ReactGroupDto;
import ru.itmo.precrimeupd.model.Criminal;
import ru.itmo.precrimeupd.model.CriminalToReactGroup;
import ru.itmo.precrimeupd.model.ReactGroup;
import ru.itmo.precrimeupd.model.UserEntity;
import ru.itmo.precrimeupd.repository.CriminalRepository;
import ru.itmo.precrimeupd.repository.CriminalToReactGroupRepository;
import ru.itmo.precrimeupd.repository.ReactGroupRepository;
import ru.itmo.precrimeupd.repository.UserRepository;
import ru.itmo.precrimeupd.security.SecurityUtil;
import ru.itmo.precrimeupd.service.ReactGroupService;
import ru.itmo.precrimeupd.service.StatisticService;
import ru.itmo.precrimeupd.service.TelegramBotService;
import ru.itmo.precrimeupd.exceptions.NotFoundException;

import java.util.List;

import static ru.itmo.precrimeupd.mapper.ReactGroupMapper.mapToReactGroup;
import static ru.itmo.precrimeupd.mapper.ReactGroupMapper.mapToReactGroupDto;

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
    public ReactGroupDto createNewGroupMember(ReactGroupDto reactGroupDto) {
        ReactGroup reactGroup = mapToReactGroup(reactGroupDto);
        ReactGroup savedGroup = reactGroupRepository.save(reactGroup);
        statisticService.createNewStatisticRecordReactGroup(reactGroup.getTelegramId());
        return mapToReactGroupDto(savedGroup);
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
        List<ReactGroup> reactGroups = reactGroupRepository.findAll();
        return reactGroups;
    }

    @Override
    public void deleteGroupMember(Long id) {
        ReactGroup reactGroupMember = findGroupMemberById(id);
        reactGroupRepository.delete(reactGroupMember);
    }

    @Override
    public ReactGroupDto updateGroupMember(Long id, ReactGroupDto reactGroupDto) {
        ReactGroup groupToUpdate = findGroupMemberById(id);
        groupToUpdate.setMemberName(reactGroupDto.getMemberName());
        groupToUpdate.setTelegramId(reactGroupDto.getTelegramId());
        ReactGroup updatedMember = reactGroupRepository.save(groupToUpdate);
        return mapToReactGroupDto(updatedMember);
    }

    @Override
    public void appointGroupToCriminal(Long id, List<Long> group) {
        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        statisticService.appointedToArrest(user);

        Criminal criminal = findCriminalById(id);
        String message = "You have been assigned to arrest a criminal, information:\n"
                + "Criminal Name: " + criminal.getName() + "\n"
                + "Last known location: " + criminal.getLocation()
                + "Weapon :" + criminal.getWeapon();
        for(Long memberId : group){
            ReactGroup groupMember = findGroupMemberById(memberId);
            int chatId = groupMember.getTelegramId();
            telegramBotService.sendMessage(chatId, message);
            CriminalToReactGroup arrestAssignment = new CriminalToReactGroup();
            arrestAssignment.setCriminal(criminal);
            arrestAssignment.setReactGroup(groupMember);
            criminalToReactGroupRepository.save(arrestAssignment);
        }
    }

}
