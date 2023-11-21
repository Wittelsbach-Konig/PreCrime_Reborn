package ru.itmo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.backend.dto.CrimeCardInDto;
import ru.itmo.backend.dto.CrimeCardOutDto;
import ru.itmo.backend.service.CardService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/cards")
public class CrimeCardController {
    private final CardService cardService;

    @Autowired
    public CrimeCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<CrimeCardOutDto>> getAllCards(){
        List<CrimeCardOutDto> crimeCards = cardService.getAllDetectiveCards();
        return new ResponseEntity<>(crimeCards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrimeCardOutDto> getCard(@PathVariable Long id) {
        CrimeCardOutDto card = cardService.getCardById(id);
        return  new ResponseEntity<>(card, HttpStatus.OK);
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
        String[] names = {"John", "Emma", "Michael", "Sophia", "William", "Adolph", "Stepan", "Roman"};
        Random random = new Random();
        int index = random.nextInt(names.length);
        return new ResponseEntity<>(names[index], HttpStatus.OK);
    }

    @PostMapping("/newcard")
    public ResponseEntity<CrimeCardOutDto> fillCard(@RequestBody CrimeCardInDto cardDto){
        CrimeCardOutDto createdCard = cardService.createCard(cardDto);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CrimeCardOutDto> updateCardData(@PathVariable Long id
                                                , @RequestBody CrimeCardInDto updatedCardDto) {
        CrimeCardOutDto cardAfterUpdate = cardService.updateCard(id, updatedCardDto);
        return new ResponseEntity<>(cardAfterUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return new ResponseEntity<>("Card successfully deleted", HttpStatus.NO_CONTENT);
    }
}
