package ru.itmo.backend.service.impl;

import org.springframework.stereotype.Service;
import ru.itmo.backend.dto.VisionDto;
import ru.itmo.backend.dto.VisionOutDto;
import ru.itmo.backend.exceptions.NotFoundException;
import ru.itmo.backend.exceptions.NotValidArgumentException;
import ru.itmo.backend.models.UserEntity;
import ru.itmo.backend.models.Vision;
import ru.itmo.backend.repository.UserRepository;
import ru.itmo.backend.repository.VisionRepository;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.StatisticService;
import ru.itmo.backend.service.VisionService;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisionServiceImpl implements VisionService {

    private final VisionRepository visionRepository;
    private final UserRepository userRepository;
    private final StatisticService statisticService;
    private final SecurityUtil securityUtil;

    public VisionServiceImpl(VisionRepository visionRepository
                            , UserRepository userRepository
                            , StatisticService statisticService
                            , SecurityUtil securityUtil) {
        this.visionRepository = visionRepository;
        this.userRepository = userRepository;
        this.statisticService = statisticService;
        this.securityUtil = securityUtil;
    }

    @Override
    public Vision findById(Long id) {
        return visionRepository.findById(id).orElseThrow(()->new NotFoundException("Vision not found: " + id));
    }

    @Override
    public VisionOutDto saveVision(VisionDto visionDto) {
        Vision existingVision = findByUrl(visionDto.getVideoUrl());
        if(existingVision != null
                && existingVision.getVideoUrl() != null
                && !existingVision.getVideoUrl().isEmpty()) {
            throw new NotValidArgumentException("Unable to add vision, such vision already exists");
        }

        Vision newVision = new Vision();
        newVision.setVideoUrl(visionDto.getVideoUrl());
        Vision savedVision = visionRepository.save(newVision);
        VisionOutDto result = VisionOutDto.builder()
                .id(savedVision.getId())
                .videoUrl(savedVision.getVideoUrl())
                .accepted(savedVision.isAccepted())
                .build();
        return result;
    }

    @Override
    public Vision findByUrl(String url) {
        return visionRepository.findByVideoUrl(url);
    }

    @Override
    public void deleteVision(Long id) {
        Vision visionToDelete = findById(id);

        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        if (visionToDelete.isAccepted()) {
            throw new NotValidArgumentException("You can't delete accepted vision!");
        }
        visionRepository.delete(visionToDelete);

        statisticService.visionRejected(user);
    }

    @Override
    public void approveVision(Long id) {
        Vision visionToApprove = findById(id);
        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        visionToApprove.setAccepted(true);
        visionRepository.save(visionToApprove);
        statisticService.visionAccepted(user);
    }

    @Override
    public List<Vision> getVisionsList() {
        List<Vision> visionList = new ArrayList<>();
        List<String> userRoles = securityUtil.getSessionUserRoles();
        if (userRoles.contains("DETECTIVE")) {
            visionList.addAll(visionRepository.findAllByAcceptedTrueAndAlreadyInUseFalse());
        }
        if (userRoles.contains("TECHNIC")) {
            visionList.addAll(visionRepository.findAllByAcceptedFalse());
        }
        return visionList;
    }

    @Override
    public List<Vision> getUsedVisionList() {
        List<Vision> visionList = new ArrayList<>();
        visionList.addAll(visionRepository.findAllByAlreadyInUseTrue());
        return visionList;
    }
}
