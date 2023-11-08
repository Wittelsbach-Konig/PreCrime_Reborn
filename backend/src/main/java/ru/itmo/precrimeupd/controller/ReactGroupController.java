package ru.itmo.precrimeupd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.precrimeupd.dto.ReactGroupDto;
import ru.itmo.precrimeupd.model.ReactGroup;
import ru.itmo.precrimeupd.service.ReactGroupService;

import java.util.List;

@RestController
public class ReactGroupController {
    private ReactGroupService reactGroupService;

    @Autowired
    public ReactGroupController(ReactGroupService reactGroupService) {
        this.reactGroupService = reactGroupService;
    }

    @GetMapping("/reactiongroup")
    public ResponseEntity<List<ReactGroup>> getAllGroups(){
        List<ReactGroup> reactGroups = reactGroupService.getAllGroups();
        return new ResponseEntity<>(reactGroups, HttpStatus.OK);
    }

    @GetMapping("/reactiongroup/{id}")
    public ResponseEntity<ReactGroup> getReactGroup(@PathVariable Long id){
        ReactGroup reactGroup = reactGroupService.findGroupById(id);
        return reactGroup != null ? new ResponseEntity<>(reactGroup, HttpStatus.OK)
                                  : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/newgroup")
    public ResponseEntity<String> addNewGroup(@RequestBody ReactGroupDto reactGroupDto){
        reactGroupService.createNewGroup(reactGroupDto);
        return new ResponseEntity<>("New group added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/reactiongroup/{id}")
    public ResponseEntity<String> updateGroupInfo(@PathVariable Long id
                                                , @RequestBody ReactGroupDto reactGroupDto) {
        ReactGroup groupToUpdate = reactGroupService.findGroupById(id);
        if(groupToUpdate == null) {
            return new ResponseEntity<>("Group does not exist", HttpStatus.NOT_FOUND);
        }
        reactGroupService.updateGroup(id, reactGroupDto);
        return new ResponseEntity<>("Group successfully updated", HttpStatus.OK);
    }

    @DeleteMapping("/reactiongroup/{id}")
    public ResponseEntity<String> deleteGroup (@PathVariable Long id) {
        ReactGroup groupToDelete = reactGroupService.findGroupById(id);
        if(groupToDelete == null) {
            return new ResponseEntity<>("Group does not exist", HttpStatus.NOT_FOUND);
        }
        reactGroupService.deleteGroup(id);
        return new ResponseEntity<>("Group successfully deleted", HttpStatus.NO_CONTENT);
    }


}
