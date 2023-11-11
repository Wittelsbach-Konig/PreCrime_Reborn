package ru.itmo.precrimeupd.service.impl;

import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.VisionDto;
import ru.itmo.precrimeupd.model.Role;
import ru.itmo.precrimeupd.model.UserEntity;
import ru.itmo.precrimeupd.model.Vision;
import ru.itmo.precrimeupd.repository.UserRepository;
import ru.itmo.precrimeupd.repository.VisionRepository;
import ru.itmo.precrimeupd.security.SecurityUtil;
import ru.itmo.precrimeupd.service.StatisticService;
import ru.itmo.precrimeupd.service.VisionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VisionServiceImpl implements VisionService {

    private VisionRepository visionRepository;
    private UserRepository userRepository;
    private StatisticService statisticService;

    public VisionServiceImpl(VisionRepository visionRepository
                            , UserRepository userRepository
                            , StatisticService statisticService) {
        this.visionRepository = visionRepository;
        this.userRepository = userRepository;
        this.statisticService = statisticService;
    }

    @Override
    public Vision findById(Long id) {
        Optional<Vision> vision = visionRepository.findById(id);
        if (vision.isPresent()) {
            return vision.get();
        }
        return null;
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
        Optional<Vision> visionToDelete = visionRepository.findById(id);
        if (visionToDelete.isPresent()){
            String login = SecurityUtil.getSessionUser();
            UserEntity user = userRepository.findByLogin(login);

            visionRepository.deleteById(id);
            statisticService.visionRejected(user);
        }
    }

    @Override
    public void approveVision(Long id) {
        Optional<Vision> visionToApprove = visionRepository.findById(id);
        if (visionToApprove.isPresent()) {
            String login = SecurityUtil.getSessionUser();
            UserEntity user = userRepository.findByLogin(login);

            Vision approvedVision = visionToApprove.get();
            approvedVision.setAccepted(true);
            visionRepository.save(approvedVision);
            statisticService.visionAccepted(user);
        }
    }

    @Override
    public List<Vision> getVisionsList() {
        List<Vision> visionList = new ArrayList<>();
        List<String> userRoles = SecurityUtil.getSessionUserRoles();
        if (userRoles.contains("DETECTIVE")) {
            visionList.addAll(visionRepository.findByAcceptedTrue());
        }
        if (userRoles.contains("TECHNIC")) {
            visionList.addAll(visionRepository.findByAcceptedFalse());
        }
        return visionList;
    }
}
