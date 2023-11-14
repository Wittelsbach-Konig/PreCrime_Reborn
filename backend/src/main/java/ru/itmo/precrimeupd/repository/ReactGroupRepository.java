package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.itmo.precrimeupd.model.ReactGroup;

import java.util.Optional;

public interface ReactGroupRepository extends JpaRepository<ReactGroup, Long> {
    Optional<ReactGroup> findById(Long id);
    ReactGroup findByTelegramId(int telegramId);
}
