package ru.itmo.precrimeupd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.precrimeupd.dto.CrimeCardDto;
import ru.itmo.precrimeupd.model.CrimeCard;
import ru.itmo.precrimeupd.service.CardService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/cards")
public class CrimeCardController {
    private CardService cardService;

    @Autowired
    public CrimeCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<CrimeCard>> getAllCards(){
        List<CrimeCard> crimeCards = cardService.getAllCards();
        return new ResponseEntity<>(crimeCards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrimeCard> getCard(@PathVariable Long id) {
        CrimeCard card = cardService.getCardById(id);
        return card != null ? new ResponseEntity<>(card, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/randomDateTime")
    public ResponseEntity<String> getRandomDateTime(){
        Random random = new Random();
        long timestamp = System.currentTimeMillis() + random.nextInt(260000000);
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return new ResponseEntity<>(sdf.format(date), HttpStatus.OK);
    }

    @GetMapping("/randomVictimName")
    public ResponseEntity<String> getRandomVictimName(){
        String[] names = {"Sergey", "Bill", "Alex", "Jane", "Alice", "Diana", "Elizabeth", "Clara", "Jack"};
        Random random = new Random();
        int index = random.nextInt(names.length);
        return new ResponseEntity<>(names[index], HttpStatus.OK);
    }

    @GetMapping("/randomCriminalName")
    public ResponseEntity<String> getRandomCriminalName(){
        String[] names = {"John", "Emma", "Michael", "Sophia", "William", "Adolph", "Stepam", "Roman"};
        Random random = new Random();
        int index = random.nextInt(names.length);
        return new ResponseEntity<>(names[index], HttpStatus.OK);
    }

    @PostMapping("/newcard")
    public ResponseEntity<String> fillCard(@RequestBody CrimeCardDto cardDto){
        cardService.createCard(cardDto);
        return new ResponseEntity<>("Card added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCardData(@PathVariable Long id
                                                , @RequestBody CrimeCardDto updatedCardDto) {
        CrimeCard card = cardService.getCardById(id);
        if(card == null){
            return new ResponseEntity<>("Card does not exist",HttpStatus.NOT_FOUND);
        }
        cardService.updateCard(id, updatedCardDto);
        return new ResponseEntity<>("Card successfully updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id) {
        CrimeCard card = cardService.getCardById(id);
        if(card == null){
            return new ResponseEntity<>("Card does not exist",HttpStatus.NOT_FOUND);
        }
        cardService.deleteCard(id);
        return  new ResponseEntity<>("Card successfully deleted", HttpStatus.NO_CONTENT);
    }
}
