package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.DetectiveStatistic;
import ru.itmo.precrimeupd.model.UserEntity;

public interface DetectiveStatisticRepository extends JpaRepository<DetectiveStatistic, Long> {
    DetectiveStatistic findByDetective(UserEntity detective);
}
