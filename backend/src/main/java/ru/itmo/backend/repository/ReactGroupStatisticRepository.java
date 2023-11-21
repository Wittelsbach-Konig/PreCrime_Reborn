package ru.itmo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.backend.models.ReactGroup;
import ru.itmo.backend.models.ReactGroupStatistic;

public interface ReactGroupStatisticRepository extends JpaRepository<ReactGroupStatistic, Long> {
    ReactGroupStatistic findByMember(ReactGroup member);
}
