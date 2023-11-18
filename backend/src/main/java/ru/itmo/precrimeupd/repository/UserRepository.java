package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByLogin(String login);
    Optional<UserEntity> findById(Long id);
    UserEntity findFirstByLogin(String login);
}
