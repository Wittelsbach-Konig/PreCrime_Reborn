package ru.itmo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.backend.models.CrimeCard;
import ru.itmo.backend.models.UserEntity;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<CrimeCard, Long> {
    Optional<CrimeCard> findById(Long id);
    List<CrimeCard> findAllByResponsibleDetective(UserEntity detective);
    List<CrimeCard> findAllByResponsibleDetectiveOrderByCreationDateAsc(UserEntity detective);
    List<CrimeCard> findAllByResponsibleDetectiveOrderByCreationDateDesc(UserEntity detective);
    List<CrimeCard> findAllByResponsibleDetectiveOrderByCrimeTimeAsc(UserEntity detective);
    List<CrimeCard> findAllByResponsibleDetectiveOrderByCrimeTimeDesc(UserEntity detective);
}
