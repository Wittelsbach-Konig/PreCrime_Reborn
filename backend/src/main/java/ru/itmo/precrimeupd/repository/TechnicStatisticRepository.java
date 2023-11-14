package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.TechnicStatistic;
import ru.itmo.precrimeupd.model.UserEntity;

public interface TechnicStatisticRepository extends JpaRepository<TechnicStatistic, Long> {
    TechnicStatistic findByTechnic(UserEntity technic);
}
