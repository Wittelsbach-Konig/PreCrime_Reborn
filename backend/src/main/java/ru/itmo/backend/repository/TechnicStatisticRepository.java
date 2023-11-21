package ru.itmo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.backend.models.TechnicStatistic;
import ru.itmo.backend.models.UserEntity;

public interface TechnicStatisticRepository extends JpaRepository<TechnicStatistic, Long> {
    TechnicStatistic findByTechnic(UserEntity technic);
}
