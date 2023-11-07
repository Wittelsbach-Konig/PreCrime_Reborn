package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.VisionDto;
import ru.itmo.precrimeupd.model.Vision;

import java.util.List;

public interface VisionService {
    Vision findById(Long id);
    void saveVision(VisionDto visionDto);
    Vision findByUrl(String url);
    void deleteVision(Long id);
    void approveVision(Long id);
    List<Vision> getVisionsList();
}
