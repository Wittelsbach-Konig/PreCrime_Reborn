package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.Transport;

public interface TransportRepository extends JpaRepository<Transport, Long> {

}
