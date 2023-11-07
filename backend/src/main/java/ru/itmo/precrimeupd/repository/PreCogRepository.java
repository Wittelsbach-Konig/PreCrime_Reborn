package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.PreCog;

public interface PreCogRepository extends JpaRepository<PreCog, Long> {
    PreCog findByPrecogName(String name);

}
