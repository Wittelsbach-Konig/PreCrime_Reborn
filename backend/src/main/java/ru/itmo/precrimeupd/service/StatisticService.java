package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.UserOutDto;
import ru.itmo.precrimeupd.dto.UserStatisticInfo;
import ru.itmo.precrimeupd.model.Criminal;
import ru.itmo.precrimeupd.model.GroupResourceType;
import ru.itmo.precrimeupd.model.UserEntity;

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

    void visionAccepted(UserEntity technic);
    void visionRejected(UserEntity technic);
    void enteredDopamine(UserEntity technic, int amount);
    void enteredSerotonine(UserEntity technic, int amount);
    void enteredDepressant(UserEntity technic, int amount);

    UserStatisticInfo getUserStatistic(Long id);
}
