package ru.itmo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.backend.dto.VisionDto;
import ru.itmo.backend.dto.VisionOutDto;
import ru.itmo.backend.models.Vision;
import ru.itmo.backend.service.VisionService;

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

    @GetMapping("/used")
    public ResponseEntity<List<Vision>> getUsedVisionsList(){
        List<Vision> visions = visionService.getUsedVisionList();
        return new ResponseEntity<>(visions, HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/accept", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> acceptVision(@PathVariable Long id){
        visionService.approveVision(id);
        return new ResponseEntity<>("Vision successfully accepted", HttpStatus.OK);
    }

    // only for ADMIN
    @PostMapping("/add")
    public ResponseEntity<VisionOutDto> addNewVision(@RequestBody VisionDto vision) {
        VisionOutDto addedVision = visionService.saveVision(vision);
        return new ResponseEntity<>(addedVision, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> deleteVision(@PathVariable Long id) {
        visionService.deleteVision(id);
        return new ResponseEntity<>("Vision successfully deleted", HttpStatus.NO_CONTENT);
    }
}
