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

import java.util.List;
import java.util.Optional;

import static ru.itmo.precrimeupd.mapper.ReactGroupMapper.mapToReactGroup;

@Service
public class ReactGroupServiceImpl implements ReactGroupService {
    private final ReactGroupRepository reactGroupRepository;
    private final TelegramBotService telegramBotService;
    private final CriminalRepository criminalRepository;
    private final StatisticService statisticService;
    private final UserRepository userRepository;
    private final CriminalToReactGroupRepository criminalToReactGroupRepository;

    @Autowired
    public ReactGroupServiceImpl(ReactGroupRepository reactGroupRepository
                                , TelegramBotService telegramBotService
                                , CriminalRepository criminalRepository
                                , StatisticService statisticService
                                , UserRepository userRepository
                                , CriminalToReactGroupRepository criminalToReactGroupRepository) {
        this.reactGroupRepository = reactGroupRepository;
        this.telegramBotService = telegramBotService;
        this.criminalRepository = criminalRepository;
        this.statisticService = statisticService;
        this.userRepository = userRepository;
        this.criminalToReactGroupRepository = criminalToReactGroupRepository;
    }

    @Override
    public void createNewGroupMember(ReactGroupDto reactGroupDto) {
        ReactGroup reactGroup = mapToReactGroup(reactGroupDto);
        reactGroupRepository.save(reactGroup);
        statisticService.createNewStatisticRecordReactGroup(reactGroup.getTelegramId());
    }

    @Override
    public ReactGroup findGroupMemberById(Long id) {
        Optional<ReactGroup> reactGroup = reactGroupRepository.findById(id);
        return reactGroup.orElse(null);
    }

    @Override
    public List<ReactGroup> getAllMembers() {
        List<ReactGroup> reactGroups = reactGroupRepository.findAll();
        return reactGroups;
    }

    @Override
    public void deleteGroupMember(Long id) {
        Optional<ReactGroup> reactGroup = reactGroupRepository.findById(id);
        if (reactGroup.isPresent()){
            reactGroupRepository.deleteById(id);
        }
    }

    @Override
    public void updateGroupMember(Long id, ReactGroupDto reactGroupDto) {
        ReactGroup groupToUpdate = reactGroupRepository.findById(id).get();
        groupToUpdate.setMemberName(reactGroupDto.getMemberName());
        groupToUpdate.setTelegramId(reactGroupDto.getTelegramId());
        reactGroupRepository.save(groupToUpdate);
    }

    @Override
    public void appointGroupToCriminal(Long id, List<Long> group) {
        String login = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        statisticService.appointedToArrest(user);

        Criminal criminal = criminalRepository.findById(id).get();
        String message = "You have been assigned to arrest a criminal, information:\n"
                + "Criminal Name: " + criminal.getName() + "\n"
                + "Last known location: " + criminal.getLocation()
                + "Weapon :" + criminal.getWeapon();
        for(Long memberId : group){
            Optional<ReactGroup> groupMember = reactGroupRepository.findById(memberId);
            if(groupMember.isPresent()){
                int chatId = groupMember.get().getTelegramId();
                telegramBotService.sendMessage(chatId, message);
                CriminalToReactGroup arrestAssignment = new CriminalToReactGroup();
                arrestAssignment.setCriminal(criminal);
                arrestAssignment.setReactGroup(groupMember.get());
                criminalToReactGroupRepository.save(arrestAssignment);
            }
        }
    }

}
