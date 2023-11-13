package ru.itmo.precrimeupd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.precrimeupd.dto.VisionDto;
import ru.itmo.precrimeupd.model.Vision;
import ru.itmo.precrimeupd.service.VisionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/visions")
public class VisionController {

    private final VisionService visionService;

    @Autowired
    public VisionController(VisionService visionService) {
        this.visionService = visionService;
    }

    @GetMapping
    public ResponseEntity<List<Vision>> getVisionsList(){
        List<Vision> visions = visionService.getVisionsList();
        return new ResponseEntity<>(visions, HttpStatus.OK);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<String> acceptVision(@PathVariable Long id){
        Vision visionToAccept = visionService.findById(id);
        if(visionToAccept == null){
            return new ResponseEntity<>("Vision does not exist", HttpStatus.NOT_FOUND);
        }
        visionService.approveVision(id);
        return new ResponseEntity<>("Vision successfully accepted", HttpStatus.OK);
    }

    // only for ADMIN
    @PostMapping("/add")
    public ResponseEntity<String> addNewVision(@RequestParam VisionDto vision) {
        Vision existingVision = visionService.findByUrl(vision.getVideoUrl());
        if(existingVision != null
                && existingVision.getVideoUrl() != null
                && !existingVision.getVideoUrl().isEmpty()) {
            return new ResponseEntity<>("Unable to add vision, such vision already exists"
                    , HttpStatus.BAD_REQUEST);
        }
        visionService.saveVision(vision);
        return new ResponseEntity<>("New vision successfully added", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVision(@PathVariable Long id){
        Vision visionToAccept = visionService.findById(id);
        if(visionToAccept == null){
            return new ResponseEntity<>("Vision does not exist", HttpStatus.NOT_FOUND);
        }
        visionService.deleteVision(id);
        return new ResponseEntity<>("Vision successfully deleted", HttpStatus.NO_CONTENT);
    }
}
