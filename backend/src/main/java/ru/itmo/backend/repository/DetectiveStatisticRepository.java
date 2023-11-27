package ru.itmo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.backend.models.DetectiveStatistic;
import ru.itmo.backend.models.UserEntity;

public interface DetectiveStatisticRepository extends JpaRepository<DetectiveStatistic, Long> {
    DetectiveStatistic findByDetective(UserEntity detective);
}
