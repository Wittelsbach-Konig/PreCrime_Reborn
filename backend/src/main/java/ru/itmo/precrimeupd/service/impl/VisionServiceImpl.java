package ru.itmo.precrimeupd.service.impl;

import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.VisionDto;
import ru.itmo.precrimeupd.exceptions.NotFoundException;
import ru.itmo.precrimeupd.model.UserEntity;
import ru.itmo.precrimeupd.model.Vision;
import ru.itmo.precrimeupd.repository.UserRepository;
import ru.itmo.precrimeupd.repository.VisionRepository;
import ru.itmo.precrimeupd.security.SecurityUtil;
import ru.itmo.precrimeupd.service.StatisticService;
import ru.itmo.precrimeupd.service.VisionService;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisionServiceImpl implements VisionService {

    private final VisionRepository visionRepository;
    private final UserRepository userRepository;
    private final StatisticService statisticService;

    public VisionServiceImpl(VisionRepository visionRepository
                            , UserRepository userRepository
                            , StatisticService statisticService) {
        this.visionRepository = visionRepository;
        this.userRepository = userRepository;
        this.statisticService = statisticService;
    }

    @Override
    public Vision findById(Long id) {
        return visionRepository.findById(id).orElseThrow(() -> new NotFoundException("Vision not found: " + id));
    }

    @Override
    public void saveVision(VisionDto visionDto) {
        Vision newVision = new Vision();
        newVision.setVideoUrl(visionDto.getVideoUrl());
        visionRepository.save(newVision);
    }

    @Override
    public Vision findByUrl(String url) {
        return visionRepository.findByVideoUrl(url);
    }

    @Override
    public void deleteVision(Long id) {
        Vision visionToDelete = findById(id);
        String login = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);

        visionRepository.delete(visionToDelete);
        statisticService.visionRejected(user);
    }

    @Override
    public void approveVision(Long id) {
        Vision visionToApprove = findById(id);
        String login = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);

        visionToApprove.setAccepted(true);
        visionRepository.save(visionToApprove);
        statisticService.visionAccepted(user);
    }

    @Override
    public List<Vision> getVisionsList() {
        List<Vision> visionList = new ArrayList<>();
        List<String> userRoles = SecurityUtil.getSessionUserRoles();
        if (userRoles.contains("DETECTIVE")) {
            visionList.addAll(visionRepository.findAllByAcceptedTrue());
        }
        if (userRoles.contains("TECHNIC")) {
            visionList.addAll(visionRepository.findAllByAcceptedFalse());
        }
        return visionList;
    }
}
