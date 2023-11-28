package ru.itmo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.backend.models.PreCog;

import java.util.List;
import java.util.Optional;

public interface PreCogRepository extends JpaRepository<PreCog, Long> {
    Optional<PreCog> findById(Long id);
    List<PreCog> findAllByIsWorkTrue();
}
