package ru.itmo.backend.service;

import ru.itmo.backend.dto.ReactGroupStatisticDto;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.dto.UserStatisticInfo;
import ru.itmo.backend.models.Criminal;
import ru.itmo.backend.models.GroupResourceType;
import ru.itmo.backend.models.UserEntity;

import java.util.List;

public interface StatisticService {
    void createNewStatisticRecordRegistration(String login);
    void createNewStatisticRecordReactGroup(int telegramId);
    List<UserOutDto> getSystemWorkers();
    void foundErrorInCard(UserEntity detective);
    void completedInvestigation(UserEntity detective);

    void orderedResource(UserEntity boss, int amount, GroupResourceType type);
    void appointedToArrest(UserEntity boss);
    void criminalArrested(Criminal criminal);
    void criminalEscaped(Criminal criminal);
    void carReFueled(UserEntity boss, int amount);

    void visionAccepted(UserEntity technic);
    void visionRejected(UserEntity technic);
    void enteredDopamine(UserEntity technic, int amount);
    void enteredSerotonine(UserEntity technic, int amount);
    void enteredDepressant(UserEntity technic, int amount);

    UserStatisticInfo getUserStatistic(Long id);
    ReactGroupStatisticDto getGroupMemberStatistic(Long id);
}
