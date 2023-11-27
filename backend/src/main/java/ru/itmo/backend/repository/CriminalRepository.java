package ru.itmo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.backend.models.CrimeCard;
import ru.itmo.backend.models.Criminal;
import ru.itmo.backend.models.CriminalStatus;

import java.util.List;

public interface CriminalRepository extends JpaRepository<Criminal, Long> {
    Criminal findByCrimeCard(CrimeCard card);
    List<Criminal> findAllByStatusIsLike(CriminalStatus status);
}
