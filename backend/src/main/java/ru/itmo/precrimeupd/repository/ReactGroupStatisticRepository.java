package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.ReactGroup;
import ru.itmo.precrimeupd.model.ReactGroupStatistic;

public interface ReactGroupStatisticRepository extends JpaRepository<ReactGroupStatistic, Long> {
    ReactGroupStatistic findByMember(ReactGroup member);
}
