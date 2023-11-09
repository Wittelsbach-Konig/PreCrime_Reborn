package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.CrimeCard;
import ru.itmo.precrimeupd.model.Criminal;
import ru.itmo.precrimeupd.model.CriminalStatus;

import java.util.List;

public interface CriminalRepository extends JpaRepository<Criminal, Long> {
    Criminal findByCrimeCard(CrimeCard card);
    List<Criminal> findByStatusIsLike(CriminalStatus status);
}
