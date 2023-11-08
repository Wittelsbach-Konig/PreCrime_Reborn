package ru.itmo.precrimeupd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import ru.itmo.precrimeupd.dto.PreCogDto;
import ru.itmo.precrimeupd.model.PreCog;
import ru.itmo.precrimeupd.service.PreCogService;

import java.util.List;

@RestController
public class PreCogController {
    private PreCogService preCogService;

    @Autowired
    public PreCogController(PreCogService preCogService) {
        this.preCogService = preCogService;
    }

    @GetMapping("/precogs")
    public ResponseEntity<List<PreCog>> getAllPreCogs() {
        List<PreCog> preCogs = preCogService.getAllPrecogs();
        return new ResponseEntity<>(preCogs, HttpStatus.OK);
    }

    @GetMapping("/precogs/{id}")
    public ResponseEntity<PreCog> getPreCog(@PathVariable Long id) {
        PreCog preCog = preCogService.getPrecog(id);
        return preCog != null ? new ResponseEntity<>(preCog, HttpStatus.OK)
                              : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/newprecog")
    public ResponseEntity<String> addNewPreCog(@RequestBody PreCogDto preCogDto) {
        // add check if precog already exists ??
        preCogService.addNewPrecog(preCogDto);
        return new ResponseEntity<>("New PreCog successfully added", HttpStatus.CREATED);
    }

    @PutMapping("/precogs/{id}")
    public ResponseEntity<String> updatePreCogInfo(@PathVariable Long id
            , @RequestBody PreCogDto preCogDto) {
        PreCog preCog = preCogService.getPrecog(id);
        // add check if precog already exists ??
        if (preCog == null){
            return new ResponseEntity<>("PreCog does not exist", HttpStatus.NOT_FOUND);
        }
        preCogService.updatePrecodInfo(id, preCogDto);
        return new ResponseEntity<>("Precog successfully updated", HttpStatus.OK);
    }

    @DeleteMapping("/precogs/{id}")
    public ResponseEntity<String> deletePreCog (@PathVariable Long id) {
        PreCog preCog = preCogService.getPrecog(id);
        if (preCog == null){
            return new ResponseEntity<>("PreCog does not exist", HttpStatus.NOT_FOUND);
        }
        preCogService.deletePrecog(id);
        return new ResponseEntity<>("PreCog successfully deleted", HttpStatus.NO_CONTENT);
    }

}
