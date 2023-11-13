package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.PreCog;

import java.util.List;
import java.util.Optional;

public interface PreCogRepository extends JpaRepository<PreCog, Long> {
    Optional<PreCog> findById(Long id);
    List<PreCog> findAllByIsWorkTrue();
}
