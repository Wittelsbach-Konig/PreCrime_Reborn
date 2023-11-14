package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.Criminal;
import ru.itmo.precrimeupd.model.CriminalToReactGroup;

import java.util.List;

public interface CriminalToReactGroupRepository extends JpaRepository<CriminalToReactGroup, Long> {
    List<CriminalToReactGroup> findAllByCriminal(Criminal criminal);
}
