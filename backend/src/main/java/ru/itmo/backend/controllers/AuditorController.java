package ru.itmo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.backend.dto.CrimeCardOutDto;
import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.dto.UserStatisticInfo;
import ru.itmo.backend.service.CardService;
import ru.itmo.backend.service.StatisticService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auditor")
public class AuditorController {

    private final StatisticService statisticService;
    private final CardService cardService;

    @Autowired
    public AuditorController(StatisticService statisticService
                            , CardService cardService) {
        this.statisticService = statisticService;
        this.cardService = cardService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserOutDto>> getAllUsers(){
        List<UserOutDto> people = statisticService.getSystemWorkers();
        if(people.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserStatisticInfo> getUserStatistic(@PathVariable Long id){
        UserStatisticInfo userStatisticInfo = statisticService.getUserStatistic(id);
        return new ResponseEntity<>(userStatisticInfo, HttpStatus.OK);
    }

    @GetMapping("/cards")
    public ResponseEntity<List<CrimeCardOutDto>> getAllCrimeCards(){
        List<CrimeCardOutDto> crimeCardOutDtos = cardService.getAllCards();
        if(crimeCardOutDtos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        System.out.println("Auditor cards size= " + crimeCardOutDtos.size());
        return new ResponseEntity<>(crimeCardOutDtos, HttpStatus.OK);
    }

    @GetMapping("/cards/bycreationdate")
    public ResponseEntity<List<CrimeCardOutDto>> getAllCrimeCardsSortedByCreationDate(@RequestParam String direction){
        List<CrimeCardOutDto> crimeCardOutDtos = cardService.getAllCardsSortedByCreationDate(direction);
        if(crimeCardOutDtos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(crimeCardOutDtos, HttpStatus.OK);
    }

    @GetMapping("/cards/bycrimetime")
    public ResponseEntity<List<CrimeCardOutDto>> getAllCrimeCardsSortedByCrimeTime(@RequestParam String direction){
        List<CrimeCardOutDto> crimeCardOutDtos = cardService.getAllCardsSortedByCrimeTime(direction);
        if(crimeCardOutDtos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(crimeCardOutDtos, HttpStatus.OK);
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<CrimeCardOutDto> getCrimeCard(@PathVariable Long id){
        CrimeCardOutDto crimeCardOutDto = cardService.getCardById(id);
        return new ResponseEntity<>(crimeCardOutDto, HttpStatus.OK);
    }

    @PostMapping(path = "/cards/{id}", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> reportMistakeInCard(@PathVariable Long id, @RequestParam String message) {
        cardService.reportCardMistake(id, message);
        return new ResponseEntity<>("Report successfully send",HttpStatus.OK);
    }
}
