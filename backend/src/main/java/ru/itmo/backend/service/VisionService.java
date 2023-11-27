package ru.itmo.backend.service;

import ru.itmo.backend.dto.VisionDto;
import ru.itmo.backend.dto.VisionOutDto;
import ru.itmo.backend.models.Vision;

import java.util.List;

public interface VisionService {
    Vision findById(Long id);
    VisionOutDto saveVision(VisionDto visionDto);
    Vision findByUrl(String url);
    void deleteVision(Long id);
    void approveVision(Long id);
    List<Vision> getVisionsList();
}
