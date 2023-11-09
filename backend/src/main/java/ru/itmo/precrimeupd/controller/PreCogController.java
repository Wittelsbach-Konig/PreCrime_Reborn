package ru.itmo.precrimeupd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        List<PreCog> preCogs = preCogService.getAllPreCogs();
        if(preCogs.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(preCogs, HttpStatus.OK);
    }

    @GetMapping("/precogs/{id}")
    public ResponseEntity<PreCog> getPreCog(@PathVariable Long id) {
        PreCog preCog = preCogService.getPreCog(id);
        return preCog != null ? new ResponseEntity<>(preCog, HttpStatus.OK)
                              : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/precogs/newprecog")
    public ResponseEntity<String> addNewPreCog(@RequestBody PreCogDto preCogDto) {
        // add check if precog already exists ??
        preCogService.addNewPreCog(preCogDto);
        return new ResponseEntity<>("New PreCog successfully added", HttpStatus.CREATED);
    }

    @PutMapping("/precogs/{id}")
    public ResponseEntity<String> updatePreCogInfo(@PathVariable Long id
                                                    , @RequestBody PreCogDto preCogDto) {
        PreCog preCog = preCogService.getPreCog(id);
        // add check if precog already exists ??
        if (preCog == null){
            return new ResponseEntity<>("PreCog does not exist", HttpStatus.NOT_FOUND);
        }
        preCogService.updatePreCogInfo(id, preCogDto);
        return new ResponseEntity<>("Precog successfully updated", HttpStatus.OK);
    }

    @PutMapping("/precogs/{id}/enterdopamine")
    public ResponseEntity<String> enterDopamineToPreCog(@PathVariable Long id, @RequestBody int amount) {
        PreCog preCog = preCogService.getPreCog(id);
        if (preCog == null){
            return new ResponseEntity<>("PreCog does not exist", HttpStatus.NOT_FOUND);
        }
        try {
            preCogService.enterDopamine(id, amount);
        } catch (IllegalArgumentException ex){
            return new ResponseEntity<>("Error while entering dopamine: " + ex.getMessage()
                    , HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Dopamine successfully added", HttpStatus.OK);
    }

    @PutMapping("/precogs/{id}/enterserotonin")
    public ResponseEntity<String> enterSerotoninToPreCog(@PathVariable Long id
                                                        , @RequestBody int amount){
        PreCog preCog = preCogService.getPreCog(id);
        if (preCog == null){
            return new ResponseEntity<>("PreCog does not exist", HttpStatus.NOT_FOUND);
        }
        try{
            preCogService.enterSerotonine(id, amount);
        } catch (IllegalArgumentException ex){
            return new ResponseEntity<>("Error while entering serotonin: " + ex.getMessage()
                    , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Serotonine successfully added", HttpStatus.OK);
    }

    @PutMapping("/precogs/{id}/enterdepressant")
    public ResponseEntity<String> enterDepressantToPreCog(@PathVariable Long id, @RequestBody int amount){
        PreCog preCog = preCogService.getPreCog(id);
        if (preCog == null){
            return new ResponseEntity<>("PreCog does not exist", HttpStatus.NOT_FOUND);
        }
        try{
            preCogService.enterDepressant(id, amount);
        } catch (IllegalArgumentException ex){
            return new ResponseEntity<>("Error while entering depressants: " + ex.getMessage()
                    , HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Depressant successfully added", HttpStatus.OK);
    }

    @PostMapping("/precogs/{id}/retire")
    public ResponseEntity<String> retirePreCog(@PathVariable Long id) {
        PreCog preCog = preCogService.getPreCog(id);
        if (preCog == null){
            return new ResponseEntity<>("PreCog does not exist", HttpStatus.NOT_FOUND);
        }
        preCogService.retirePreCog(id);
        return new ResponseEntity<>("Precog successfully retired", HttpStatus.OK);
    }

    @PostMapping("/precogs/{id}/rehabilitate")
    public ResponseEntity<String> rehabilitatePreCog(@PathVariable Long id) {
        PreCog preCog = preCogService.getPreCog(id);
        if (preCog == null){
            return new ResponseEntity<>("PreCog does not exist", HttpStatus.NOT_FOUND);
        }
        preCogService.rehabilitatePreCog(id);
        return new ResponseEntity<>("Precog successfullt rehabilitated", HttpStatus.OK);
    }

    @DeleteMapping("/precogs/{id}")
    public ResponseEntity<String> deletePreCog (@PathVariable Long id) {
        PreCog preCog = preCogService.getPreCog(id);
        if (preCog == null){
            return new ResponseEntity<>("PreCog does not exist", HttpStatus.NOT_FOUND);
        }
        preCogService.deletePreCog(id);
        return new ResponseEntity<>("PreCog successfully deleted", HttpStatus.NO_CONTENT);
    }

}
