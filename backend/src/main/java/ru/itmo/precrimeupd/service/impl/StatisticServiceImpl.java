package ru.itmo.precrimeupd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.*;
import ru.itmo.precrimeupd.model.*;
import ru.itmo.precrimeupd.repository.*;
import ru.itmo.precrimeupd.service.StatisticService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.itmo.precrimeupd.mapper.BossStatisticMapper.mapToBossStatisticDto;
import static ru.itmo.precrimeupd.mapper.DetectiveStatisticMapper.mapToDetectiveStatisticDto;
import static ru.itmo.precrimeupd.mapper.TechnicStatisticMapper.mapToTechnicStatisticDto;
import static ru.itmo.precrimeupd.mapper.UserInfoMapper.mapToUserInfo;

@Service
public class StatisticServiceImpl implements StatisticService {

    private BossStatisticRepository bossStatisticRepository;
    private DetectiveStatisticRepository detectiveStatisticRepository;
    private ReactGroupStatisticRepository reactGroupStatisticRepository;
    private TechnicStatisticRepository technicStatisticRepository;
    private UserRepository userRepository;
    private ReactGroupRepository reactGroupRepository;
    private CriminalToReactGroupRepository criminalToReactGroupRepository;

    @Autowired
    public StatisticServiceImpl(BossStatisticRepository bossStatisticRepository
                                , DetectiveStatisticRepository detectiveStatisticRepository
                                , ReactGroupStatisticRepository reactGroupStatisticRepository
                                , TechnicStatisticRepository technicStatisticRepository
                                , UserRepository userRepository
                                , ReactGroupRepository reactGroupRepository
                                , CriminalToReactGroupRepository criminalToReactGroupRepository) {
        this.bossStatisticRepository = bossStatisticRepository;
        this.detectiveStatisticRepository = detectiveStatisticRepository;
        this.reactGroupStatisticRepository = reactGroupStatisticRepository;
        this.technicStatisticRepository = technicStatisticRepository;
        this.userRepository = userRepository;
        this.reactGroupRepository = reactGroupRepository;
        this.criminalToReactGroupRepository = criminalToReactGroupRepository;
    }

    @Override
    public void createNewStatisticRecordRegistration(String login) {
        UserEntity user = userRepository.findByLogin(login);
        if(user != null){
            Set<Role> userRoles = user.getRoles();
            for(Role role : userRoles){
                String roleName = role.getName();
                switch (roleName){
                    case "DETECTIVE":
                        DetectiveStatistic detectiveStatistic = new DetectiveStatistic();
                        detectiveStatistic.setDetective(user);
                        detectiveStatisticRepository.save(detectiveStatistic);
                        break;
                    case "TECHNIC":
                        TechnicStatistic technicStatistic = new TechnicStatistic();
                        technicStatistic.setTechnic(user);
                        technicStatisticRepository.save(technicStatistic);
                        break;
                    case "GROUP_BOSS":
                        BossReactGroupStatistic bossReactGroupStatistic = new BossReactGroupStatistic();
                        bossReactGroupStatistic.setBoss(user);
                        bossStatisticRepository.save(bossReactGroupStatistic);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void createNewStatisticRecordReactGroup(int telegramId) {
        ReactGroup groupMember = reactGroupRepository.findByTelegramId(telegramId);
        if(groupMember != null){
            ReactGroupStatistic reactGroupStatistic = new ReactGroupStatistic();
            reactGroupStatistic.setMember(groupMember);
            reactGroupStatisticRepository.save(reactGroupStatistic);
        }

    }

    @Override
    public List<UserEntity> getSystemWorkers() {
        return userRepository.findAll();
    }

    @Override
    public void foundErrorInCard(UserEntity detective) {
        DetectiveStatistic detectiveStatistic = detectiveStatisticRepository.findByDetective(detective);
        if(detectiveStatistic != null){
            detectiveStatistic.setErrorsInCards(detectiveStatistic.getErrorsInCards() + 1);
            detectiveStatisticRepository.save(detectiveStatistic);
        }
    }

    @Override
    public void completedInvestigation(UserEntity detective) {
        DetectiveStatistic detectiveStatistic = detectiveStatisticRepository.findByDetective(detective);
        if(detectiveStatistic != null){
            detectiveStatistic.setInvestigationCount(detectiveStatistic.getInvestigationCount() + 1);
            detectiveStatisticRepository.save(detectiveStatistic);
        }
    }

    @Override
    public void orderedResource(UserEntity boss, int amount, GroupResourceType type) {
        BossReactGroupStatistic bossReactGroupStatistic = bossStatisticRepository.findByBoss(boss);
        if(bossReactGroupStatistic != null) {
            switch (type){
                case FUEL:
                    bossReactGroupStatistic.setFuelOrdered(bossReactGroupStatistic.getFuelOrdered() + amount);
                    bossStatisticRepository.save(bossReactGroupStatistic);
                    break;
                case GADGET:
                    bossReactGroupStatistic.setGadgetOrdered(bossReactGroupStatistic.getGadgetOrdered() + amount);
                    bossStatisticRepository.save(bossReactGroupStatistic);
                    break;
                case WEAPON:
                    bossReactGroupStatistic.setWeaponOrdered(bossReactGroupStatistic.getWeaponOrdered()+amount);
                    bossStatisticRepository.save(bossReactGroupStatistic);
                    break;
                case AMMUNITION:
                    bossReactGroupStatistic.setAmmoOrdered(bossReactGroupStatistic.getAmmoOrdered()+amount);
                    bossStatisticRepository.save(bossReactGroupStatistic);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void appointedToArrest(UserEntity boss) {
        BossReactGroupStatistic bossReactGroupStatistic = bossStatisticRepository.findByBoss(boss);
        if(bossReactGroupStatistic != null){
            bossReactGroupStatistic.setArresstAppointed(bossReactGroupStatistic.getArresstAppointed()+ 1);
            bossStatisticRepository.save(bossReactGroupStatistic);
        }
    }

    @Override
    public void criminalArrested(Criminal criminal) {
        List<CriminalToReactGroup> criminalToReactGroups = criminalToReactGroupRepository.findAllByCriminal(criminal);
        for(CriminalToReactGroup record : criminalToReactGroups) {
            ReactGroup member = record.getReactGroup();
            ReactGroupStatistic reactGroupStatistic = reactGroupStatisticRepository.findByMember(member);
            if(reactGroupStatistic != null) {
                reactGroupStatistic.setCriminalsCaught(reactGroupStatistic.getCriminalsCaught() + 1);
                reactGroupStatisticRepository.save(reactGroupStatistic);
            }
        }
    }

    @Override
    public void criminalEscaped(Criminal criminal) {
        List<CriminalToReactGroup> criminalToReactGroups = criminalToReactGroupRepository.findAllByCriminal(criminal);
        for(CriminalToReactGroup record : criminalToReactGroups) {
            ReactGroup member = record.getReactGroup();
            ReactGroupStatistic reactGroupStatistic = reactGroupStatisticRepository.findByMember(member);
            if(reactGroupStatistic != null) {
                reactGroupStatistic.setCriminalsEscaped(reactGroupStatistic.getCriminalsEscaped() + 1);
                reactGroupStatisticRepository.save(reactGroupStatistic);
            }
        }
    }

    @Override
    public void visionAccepted(UserEntity technic) {
        TechnicStatistic technicStatistic = technicStatisticRepository.findByTechnic(technic);
        if(technicStatistic != null){
            technicStatistic.setVisionsAccepted(technicStatistic.getVisionsAccepted()+1);
            technicStatisticRepository.save(technicStatistic);
        }
    }

    @Override
    public void visionRejected(UserEntity technic) {
        TechnicStatistic technicStatistic = technicStatisticRepository.findByTechnic(technic);
        if(technicStatistic != null){
            technicStatistic.setVisionsRejected(technicStatistic.getVisionsRejected() + 1);
            technicStatisticRepository.save(technicStatistic);
        }
    }

    @Override
    public void enteredDopamine(UserEntity technic, int amount) {
        TechnicStatistic technicStatistic = technicStatisticRepository.findByTechnic(technic);
        if(technicStatistic != null){
            technicStatistic.setDopamineEntered(technicStatistic.getDopamineEntered() + 1);
            technicStatisticRepository.save(technicStatistic);
        }
    }

    @Override
    public void enteredSerotonine(UserEntity technic, int amount) {
        TechnicStatistic technicStatistic = technicStatisticRepository.findByTechnic(technic);
        if(technicStatistic != null){
            technicStatistic.setSerotoninEntered(technicStatistic.getSerotoninEntered() + 1);
            technicStatisticRepository.save(technicStatistic);
        }
    }

    @Override
    public void enteredDepressant(UserEntity technic, int amount) {
        TechnicStatistic technicStatistic = technicStatisticRepository.findByTechnic(technic);
        if(technicStatistic != null){
            technicStatistic.setDepressantEntered(technicStatistic.getDepressantEntered() + 1);
            technicStatisticRepository.save(technicStatistic);
        }
    }

    @Override
    public UserStatisticInfo getUserStatistic(Long id) {
        UserStatisticInfo userStatisticInfo = new UserStatisticInfo();
        UserEntity user = userRepository.findById(id).get();
        UserInfoDto userInfo = mapToUserInfo(user);
        userStatisticInfo.setUserInfo(userInfo);
        userStatisticInfo.setBossReactGroupStatistic(Optional.empty());
        BossReactGroupStatistic bossReactGroupStatistic = bossStatisticRepository.findByBoss(user);
        if(bossReactGroupStatistic != null){
            BossReactGroupStatisticDto bossReactGroupStatisticDto = mapToBossStatisticDto(bossReactGroupStatistic);
            userStatisticInfo.setBossReactGroupStatistic(Optional.of(bossReactGroupStatisticDto));
        }
        userStatisticInfo.setDetectiveStatistic(Optional.empty());
        DetectiveStatistic detectiveStatistic = detectiveStatisticRepository.findByDetective(user);
        if (detectiveStatistic != null) {
            DetectiveStatisticDto detectiveStatisticDto = mapToDetectiveStatisticDto(detectiveStatistic);
            userStatisticInfo.setDetectiveStatistic(Optional.of(detectiveStatisticDto));
        }
        userStatisticInfo.setTechnicStatistic(Optional.empty());
        TechnicStatistic technicStatistic = technicStatisticRepository.findByTechnic(user);
        if(technicStatistic != null){
            TechnicStatisticDto technicStatisticDto = mapToTechnicStatisticDto(technicStatistic);
            userStatisticInfo.setTechnicStatistic(Optional.of(technicStatisticDto));
        }
        return userStatisticInfo;
    }
}
