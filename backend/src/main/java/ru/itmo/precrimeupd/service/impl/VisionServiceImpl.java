package ru.itmo.precrimeupd.service.impl;

import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.VisionDto;
import ru.itmo.precrimeupd.model.Role;
import ru.itmo.precrimeupd.model.Vision;
import ru.itmo.precrimeupd.repository.VisionRepository;
import ru.itmo.precrimeupd.security.SecurityUtil;
import ru.itmo.precrimeupd.service.VisionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VisionServiceImpl implements VisionService {

    private VisionRepository visionRepository;

    public VisionServiceImpl(VisionRepository visionRepository) {
        this.visionRepository = visionRepository;
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
            visionRepository.deleteById(id);
        }
    }

    @Override
    public void approveVision(Long id) {
        Optional<Vision> visionToApprove = visionRepository.findById(id);
        if (visionToApprove.isPresent()) {
            Vision approvedVision = visionToApprove.get();
            approvedVision.setAccepted(true);
            visionRepository.save(approvedVision);
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
