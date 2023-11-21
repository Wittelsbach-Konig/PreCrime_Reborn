package ru.itmo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.backend.dto.PreCogDto;
import ru.itmo.backend.models.PreCog;
import ru.itmo.backend.service.PreCogService;

import java.util.List;

@RequestMapping("/api/v1/precogs")
@RestController
public class PreCogController {
    private final PreCogService preCogService;

    @Autowired
    public PreCogController(PreCogService preCogService) {
        this.preCogService = preCogService;
    }

    @GetMapping
    public ResponseEntity<List<PreCog>> getAllPreCogs() {
        List<PreCog> preCogs = preCogService.getAllPreCogs();
        if(preCogs.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(preCogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreCog> getPreCog(@PathVariable Long id) {
        PreCog preCog = preCogService.getPreCog(id);
        return new ResponseEntity<>(preCog, HttpStatus.OK);
    }

    @PostMapping("/newprecog")
    public ResponseEntity<PreCog> addNewPreCog(@RequestBody PreCogDto preCogDto) {
        PreCog newPreCog = preCogService.addNewPreCog(preCogDto);
        return new ResponseEntity<>(newPreCog, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreCog> updatePreCogInfo(@PathVariable Long id
                                                    , @RequestBody PreCogDto preCogDto) {
        PreCog updatedPreCog = preCogService.updatePreCogInfo(id, preCogDto);
        return new ResponseEntity<>(updatedPreCog, HttpStatus.OK);
    }

    @PutMapping("/{id}/enterdopamine")
    public ResponseEntity<String> enterDopamineToPreCog(@PathVariable Long id, @RequestBody int amount) {
        preCogService.enterDopamine(id, amount);
        return new ResponseEntity<>("Dopamine successfully added", HttpStatus.OK);
    }

    @PutMapping("/{id}/enterserotonin")
    public ResponseEntity<String> enterSerotoninToPreCog(@PathVariable Long id
                                                        , @RequestBody int amount){
        preCogService.enterSerotonine(id, amount);
        return new ResponseEntity<>("Serotonine successfully added", HttpStatus.OK);
    }

    @PutMapping("/{id}/enterdepressant")
    public ResponseEntity<String> enterDepressantToPreCog(@PathVariable Long id, @RequestBody int amount){
        preCogService.enterDepressant(id, amount);
        return new ResponseEntity<>("Depressant successfully added", HttpStatus.OK);
    }

    @PostMapping("/{id}/retire")
    public ResponseEntity<PreCog> retirePreCog(@PathVariable Long id) {
        PreCog retiredPreCog = preCogService.retirePreCog(id);
        return new ResponseEntity<>(retiredPreCog, HttpStatus.OK);
    }

    @PostMapping("/{id}/rehabilitate")
    public ResponseEntity<PreCog> rehabilitatePreCog(@PathVariable Long id) {
        PreCog rehabilitatedPreCog = preCogService.rehabilitatePreCog(id);
        return new ResponseEntity<>(rehabilitatedPreCog, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePreCog (@PathVariable Long id) {
        preCogService.deletePreCog(id);
        return new ResponseEntity<>("PreCog successfully deleted", HttpStatus.NO_CONTENT);
    }

}
