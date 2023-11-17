package ru.itmo.precrimeupd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.precrimeupd.dto.PreCogDto;
import ru.itmo.precrimeupd.model.PreCog;
import ru.itmo.precrimeupd.service.PreCogService;

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
        return new ResponseEntity<>(preCogService.getPreCog(id), HttpStatus.OK);
    }

    @PostMapping("/newprecog")
    public ResponseEntity<String> addNewPreCog(@RequestBody PreCogDto preCogDto) {
        // add check if precog already exists ??
        preCogService.addNewPreCog(preCogDto);
        return new ResponseEntity<>("New PreCog successfully added", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePreCogInfo(@PathVariable Long id
                                                    , @RequestBody PreCogDto preCogDto) {
        // add check if precog already exists ??
        preCogService.updatePreCogInfo(id, preCogDto);
        return new ResponseEntity<>("PreCog successfully updated", HttpStatus.OK);
    }

    @PutMapping("/{id}/enterdopamine")
    public ResponseEntity<String> enterDopamineToPreCog(@PathVariable Long id, @RequestBody int amount) {
        try {
            preCogService.enterDopamine(id, amount);
        } catch (IllegalArgumentException ex){
            return new ResponseEntity<>("Error while entering dopamine: " + ex.getMessage()
                    , HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Dopamine successfully added", HttpStatus.OK);
    }

    @PutMapping("/{id}/enterserotonin")
    public ResponseEntity<String> enterSerotoninToPreCog(@PathVariable Long id
                                                        , @RequestBody int amount){
        try{
            preCogService.enterSerotonine(id, amount);
        } catch (IllegalArgumentException ex){
            return new ResponseEntity<>("Error while entering serotonin: " + ex.getMessage()
                    , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Serotonine successfully added", HttpStatus.OK);
    }

    @PutMapping("/{id}/enterdepressant")
    public ResponseEntity<String> enterDepressantToPreCog(@PathVariable Long id, @RequestBody int amount){
        try{
            preCogService.enterDepressant(id, amount);
        } catch (IllegalArgumentException ex){
            return new ResponseEntity<>("Error while entering depressants: " + ex.getMessage()
                    , HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Depressant successfully added", HttpStatus.OK);
    }

    @PostMapping("/{id}/retire")
    public ResponseEntity<String> retirePreCog(@PathVariable Long id) {
        preCogService.retirePreCog(id);
        return new ResponseEntity<>("PreCog successfully retired", HttpStatus.OK);
    }

    @PostMapping("/{id}/rehabilitate")
    public ResponseEntity<String> rehabilitatePreCog(@PathVariable Long id) {
        preCogService.rehabilitatePreCog(id);
        return new ResponseEntity<>("PreCog successfully rehabilitated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePreCog (@PathVariable Long id) {
        preCogService.deletePreCog(id);
        return new ResponseEntity<>("PreCog successfully deleted", HttpStatus.NO_CONTENT);
    }

}
