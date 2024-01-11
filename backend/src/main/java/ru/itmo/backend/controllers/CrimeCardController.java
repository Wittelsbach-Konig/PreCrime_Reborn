package ru.itmo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.backend.dto.CrimeCardInDto;
import ru.itmo.backend.dto.CrimeCardOutDto;
import ru.itmo.backend.dto.CrimeCardUpdateDto;
import ru.itmo.backend.service.CardService;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/cards")
@Validated
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

    @GetMapping("/bycreationdate")
    public ResponseEntity<List<CrimeCardOutDto>> getAllCardsSortedByCreationDate(@RequestParam String direction) {
        List<CrimeCardOutDto> crimeCards = cardService.getAllDetectiveCardsSortedByCreationDate(direction);
        return new ResponseEntity<>(crimeCards, HttpStatus.OK);
    }

    @GetMapping("/bycrimetime")
    public ResponseEntity<List<CrimeCardOutDto>> getAllCardsSortedByCrimeTime(@RequestParam String direction) {
        List<CrimeCardOutDto> crimeCards = cardService.getAllDetectiveCardsSortedByCrimeTime(direction);
        return new ResponseEntity<>(crimeCards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrimeCardOutDto> getCard(@PathVariable Long id) {
        CrimeCardOutDto card = cardService.getCardById(id);
        return  new ResponseEntity<>(card, HttpStatus.OK);
    }

    @GetMapping(path = "/randomDateTime", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> getRandomDateTime(){
        Random random = new Random();
        long timestamp = System.currentTimeMillis() + random.nextInt(260000000);
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return new ResponseEntity<>(sdf.format(date), HttpStatus.OK);
    }

    @GetMapping(path = "/randomVictimName", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> getRandomVictimName(){
        String[] names = {"Sergey", "Bill", "Alex", "Jane", "Alice", "Diana", "Elizabeth", "Clara", "Jack"};
        Random random = new Random();
        int index = random.nextInt(names.length);
        return new ResponseEntity<>(names[index], HttpStatus.OK);
    }

    @GetMapping(path= "/randomCriminalName", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> getRandomCriminalName(){
        String[] names = {"John", "Emma", "Michael", "Sophia", "William", "Adolph", "Stepan", "Roman"};
        Random random = new Random();
        int index = random.nextInt(names.length);
        return new ResponseEntity<>(names[index], HttpStatus.OK);
    }

    @PostMapping("/newcard")
    public ResponseEntity<CrimeCardOutDto> fillCard(@Valid @RequestBody CrimeCardInDto cardDto){
        CrimeCardOutDto createdCard = cardService.createCard(cardDto);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CrimeCardOutDto> updateCardData(@PathVariable Long id
                                                        , @Valid @RequestBody CrimeCardUpdateDto updatedCardDto) {
        CrimeCardOutDto cardAfterUpdate = cardService.updateCard(id, updatedCardDto);
        return new ResponseEntity<>(cardAfterUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return new ResponseEntity<>("Card successfully deleted", HttpStatus.NO_CONTENT);
    }
}
