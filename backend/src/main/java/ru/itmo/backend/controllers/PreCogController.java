package ru.itmo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.backend.dto.PreCogDto;
import ru.itmo.backend.dto.PreCogOutDto;
import ru.itmo.backend.service.PreCogService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/v1/precogs")
@RestController
@Validated
public class PreCogController {
    private final PreCogService preCogService;

    @Autowired
    public PreCogController(PreCogService preCogService) {
        this.preCogService = preCogService;
    }

    @GetMapping
    public ResponseEntity<List<PreCogOutDto>> getAllPreCogs() {
        List<PreCogOutDto> preCogs = preCogService.getAllPreCogs();
        if(preCogs.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(preCogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreCogOutDto> getPreCog(@PathVariable Long id) {
        PreCogOutDto preCog = preCogService.getPreCog(id);
        return new ResponseEntity<>(preCog, HttpStatus.OK);
    }

    @PostMapping("/newprecog")
    public ResponseEntity<PreCogOutDto> addNewPreCog(@Valid @RequestBody PreCogDto preCogDto) {
        PreCogOutDto newPreCog = preCogService.addNewPreCog(preCogDto);
        return new ResponseEntity<>(newPreCog, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreCogOutDto> updatePreCogInfo(@PathVariable Long id
                                                    , @Valid @RequestBody PreCogDto preCogDto) {
        PreCogOutDto updatedPreCog = preCogService.updatePreCogInfo(id, preCogDto);
        return new ResponseEntity<>(updatedPreCog, HttpStatus.OK);
    }

    @PutMapping("/{id}/enterdopamine")
    public ResponseEntity<String> enterDopamineToPreCog(@PathVariable Long id, @RequestParam int amount) {
        preCogService.enterDopamine(id, amount);
        return new ResponseEntity<>("Dopamine successfully added", HttpStatus.OK);
    }

    @PutMapping("/{id}/enterserotonin")
    public ResponseEntity<String> enterSerotoninToPreCog(@PathVariable Long id
                                                        , @RequestParam int amount){
        preCogService.enterSerotonine(id, amount);
        return new ResponseEntity<>("Serotonine successfully added", HttpStatus.OK);
    }

    @PutMapping("/{id}/enterdepressant")
    public ResponseEntity<String> enterDepressantToPreCog(@PathVariable Long id, @RequestParam int amount){
        preCogService.enterDepressant(id, amount);
        return new ResponseEntity<>("Depressant successfully added", HttpStatus.OK);
    }

    @PostMapping("/{id}/retire")
    public ResponseEntity<PreCogOutDto> retirePreCog(@PathVariable Long id) {
        PreCogOutDto retiredPreCog = preCogService.retirePreCog(id);
        return new ResponseEntity<>(retiredPreCog, HttpStatus.OK);
    }

    @PostMapping("/{id}/rehabilitate")
    public ResponseEntity<PreCogOutDto> rehabilitatePreCog(@PathVariable Long id) {
        PreCogOutDto rehabilitatedPreCog = preCogService.rehabilitatePreCog(id);
        return new ResponseEntity<>(rehabilitatedPreCog, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePreCog (@PathVariable Long id) {
        preCogService.deletePreCog(id);
        return new ResponseEntity<>("PreCog successfully deleted", HttpStatus.NO_CONTENT);
    }

}
