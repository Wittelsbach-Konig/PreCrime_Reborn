package ru.itmo.backend.util;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import ru.itmo.backend.models.Vision;
import ru.itmo.backend.repository.VisionRepository;

import java.util.Optional;

@Component
@Profile("jmeter")
@AllArgsConstructor
public class DbVisionInitializer {
    private final VisionRepository visionRepository;

    @EventListener(ApplicationStartedEvent.class)
    public void addVisions() {
        for (int i = 0; i < 1000; i++) {
            Vision vision = Vision.builder()
                    .videoUrl("http://www.youtube.com/" + i)
                    .accepted(false)
                    .build();
            Optional<Vision> visionOptional = visionRepository.findOne(
                    Example.of(vision)
            );
            if (visionOptional.isEmpty()) {
                visionRepository.save(vision);
            }
        }
    }
}
