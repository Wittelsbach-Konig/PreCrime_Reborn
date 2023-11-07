package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.CrimeType;

public interface CrimeTypeRepository extends JpaRepository<CrimeType, Long> {
    CrimeType findByName(String name);
}
