package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.Vision;

import java.util.List;

public interface VisionRepository extends JpaRepository<Vision, Long> {
    Vision findByVideoUrl(String videoUrl);
    List <Vision> findAllByAcceptedFalse();
    List <Vision> findAllByAcceptedTrue();

}
