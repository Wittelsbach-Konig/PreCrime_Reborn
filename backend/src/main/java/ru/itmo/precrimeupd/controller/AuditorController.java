package ru.itmo.precrimeupd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.precrimeupd.dto.UserStatisticInfo;
import ru.itmo.precrimeupd.model.UserEntity;
import ru.itmo.precrimeupd.service.StatisticService;
import ru.itmo.precrimeupd.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auditor")
public class AuditorController {

    private StatisticService statisticService;
    private UserService userService;

    @Autowired
    public AuditorController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        List<UserEntity> people = statisticService.getSystemWorkers();
        if(people.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserStatisticInfo> getUserStatistic(@PathVariable Long id){
        UserEntity userEntity = userService.findById(id);
        if(userEntity == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserStatisticInfo userStatisticInfo = statisticService.getUserStatistic(id);
        return new ResponseEntity<>(userStatisticInfo, HttpStatus.OK);
    }
}
