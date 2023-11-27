package ru.itmo.backend.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.itmo.backend.models.Vision;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class VisionRepositoryTests {
    @Autowired
    private VisionRepository visionRepository;

    @Test
    public void VisionRepository_SaveVision_ReturnVision(){
        Vision vision = new Vision();
        vision.setVideoUrl("http://www.youtube.com/1");
        Vision savedVision = visionRepository.save(vision);
        Assertions.assertNotNull(savedVision);
    }

    @Test
    public void VisionRepository_SaveVisionNotValidUrl_ThrowsException(){
        Vision vision = new Vision();
        Assertions.assertThrows(ConstraintViolationException.class, ()->{
            visionRepository.save(vision);
        });
    }

    @Test
    public void VisionRepository_SaveDuplicatedVisions_ThrowsException() {
        Vision vision = new Vision();
        vision.setVideoUrl("http://www.youtube.com/1");
        visionRepository.save(vision);
        Vision vision1 = new Vision();
        vision1.setVideoUrl("http://www.youtube.com/1");
        Assertions.assertThrows(DataIntegrityViolationException.class, ()->{
            visionRepository.save(vision1);
        });
    }

    @Test
    public void VisionRepository_FindByVisionUrl_ReturnsVision(){
        Vision vision = new Vision();
        vision.setVideoUrl("http://www.youtube.com/1");
        visionRepository.save(vision);
        Vision foundVision = visionRepository.findByVideoUrl(vision.getVideoUrl());
        Assertions.assertNotNull(foundVision);
    }

    @Test
    public void VisionRepository_FindAllByAcceptedTrue_ReturnsListVision(){
        Vision vision = new Vision();
        vision.setVideoUrl("http://www.youtube.com/1");
        vision.setAccepted(true);
        visionRepository.save(vision);
        Vision vision1 = new Vision();
        vision1.setVideoUrl("http://www.youtube.com/2");
        vision1.setAccepted(true);
        visionRepository.save(vision1);
        List<Vision> expectedVisions = Arrays.asList(vision, vision1);
        List<Vision> foundVisions = visionRepository.findAllByAcceptedTrue();
        Assertions.assertNotNull(foundVisions);
        Assertions.assertEquals(expectedVisions.size(), foundVisions.size());
        Assertions.assertEquals(expectedVisions, foundVisions);
    }

    @Test
    public void VisionRepository_FindAllByAcceptedFalse_ReturnsListVision(){
        Vision vision = new Vision();
        vision.setVideoUrl("http://www.youtube.com/1");
        vision.setAccepted(false);
        visionRepository.save(vision);
        Vision vision1 = new Vision();
        vision1.setVideoUrl("http://www.youtube.com/2");
        vision1.setAccepted(false);
        visionRepository.save(vision1);
        List<Vision> expectedVisions = Arrays.asList(vision, vision1);
        List<Vision> foundVisions = visionRepository.findAllByAcceptedFalse();
        Assertions.assertNotNull(foundVisions);
        Assertions.assertEquals(expectedVisions.size(), foundVisions.size());
        Assertions.assertEquals(expectedVisions, foundVisions);
    }

}
