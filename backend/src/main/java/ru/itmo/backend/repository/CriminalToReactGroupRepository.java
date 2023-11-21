package ru.itmo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.backend.models.Criminal;
import ru.itmo.backend.models.CriminalToReactGroup;

import java.util.List;

public interface CriminalToReactGroupRepository extends JpaRepository<CriminalToReactGroup, Long> {
    List<CriminalToReactGroup> findAllByCriminal(Criminal criminal);
}
