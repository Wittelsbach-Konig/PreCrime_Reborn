package ru.itmo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.backend.models.Transport;

public interface TransportRepository extends JpaRepository<Transport, Long> {

}
