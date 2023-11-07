package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.CrimeCard;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CrimeCard, Long> {
    Optional<CrimeCard> findById(Long id);

}
