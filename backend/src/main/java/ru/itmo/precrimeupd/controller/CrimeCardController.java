package ru.itmo.precrimeupd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.precrimeupd.dto.CrimeCardDto;
import ru.itmo.precrimeupd.model.CrimeCard;
import ru.itmo.precrimeupd.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CrimeCardController {
    private CardService cardService;

    @Autowired
    public CrimeCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/newcard")
    public ResponseEntity<String> fillCard(@RequestBody CrimeCardDto cardDto){
        cardService.createCard(cardDto);
        return new ResponseEntity<>("Card added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/cardslist")
    public ResponseEntity<List<CrimeCard>> getAllCards(){
        List<CrimeCard> crimeCards = cardService.getAllCards();
        return new ResponseEntity<>(crimeCards, HttpStatus.OK);
    }

    @GetMapping("/cardslist/{id}")
    public ResponseEntity<CrimeCard> getCard(@PathVariable Long id) {
        CrimeCard card = cardService.getCardById(id);
        return card != null ? new ResponseEntity<>(card, HttpStatus.OK)
                            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/cardslist/{id}")
    public ResponseEntity<String> updateCardData(@PathVariable Long id
                                                , @RequestBody CrimeCardDto updatedCardDto) {
        CrimeCard card = cardService.getCardById(id);
        if(card == null){
            return new ResponseEntity<>("Card does not exist",HttpStatus.NOT_FOUND);
        }
        cardService.updateCard(id, updatedCardDto);
        return new ResponseEntity<>("Card successfully updated", HttpStatus.OK);
    }

    @DeleteMapping("/cardslist/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id) {
        CrimeCard card = cardService.getCardById(id);
        if(card == null){
            return new ResponseEntity<>("Card does not exist",HttpStatus.NOT_FOUND);
        }
        cardService.deleteCard(id);
        return  new ResponseEntity<>("Card successfully deleted", HttpStatus.NO_CONTENT);
    }

}
