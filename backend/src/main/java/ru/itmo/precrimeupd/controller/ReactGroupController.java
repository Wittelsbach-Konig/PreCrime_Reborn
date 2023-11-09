package ru.itmo.precrimeupd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import ru.itmo.precrimeupd.dto.ReactGroupDto;
import ru.itmo.precrimeupd.dto.ResourceDto;
import ru.itmo.precrimeupd.model.Criminal;
import ru.itmo.precrimeupd.model.CriminalStatus;
import ru.itmo.precrimeupd.model.GroupResource;
import ru.itmo.precrimeupd.model.ReactGroup;
import ru.itmo.precrimeupd.service.CardService;
import ru.itmo.precrimeupd.service.GroupResourceService;
import ru.itmo.precrimeupd.service.ReactGroupService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reactiongroup")
public class ReactGroupController {
    private ReactGroupService reactGroupService;
    private CardService cardService;
    private GroupResourceService groupResourceService;

    @Autowired
    public ReactGroupController(ReactGroupService reactGroupService
                                , CardService cardService
                                , GroupResourceService groupResourceService) {
        this.reactGroupService = reactGroupService;
        this.cardService = cardService;
        this.groupResourceService = groupResourceService;
    }

    @GetMapping
    public ResponseEntity<List<ReactGroup>> getAllGroups(){
        List<ReactGroup> reactGroups = reactGroupService.getAllMembers();
        return new ResponseEntity<>(reactGroups, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReactGroup> getReactGroup(@PathVariable Long id){
        ReactGroup reactGroup = reactGroupService.findGroupMemberById(id);
        return reactGroup != null ? new ResponseEntity<>(reactGroup, HttpStatus.OK)
                                  : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/criminal")
    public ResponseEntity<List<Criminal>> getAllCriminals() {
        List<Criminal> criminals = cardService.getAllCriminals();
        if(criminals.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(criminals, HttpStatus.OK);
    }

    @GetMapping("/criminal/{id}")
    public ResponseEntity<Criminal> getCriminal(@PathVariable Long id){
        Criminal criminal = cardService.getCriminalById(id);
        return criminal != null ? new ResponseEntity<>(criminal, HttpStatus.OK)
                                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/supply")
    public ResponseEntity<List<GroupResource>> getResources() {
        List<GroupResource> resources = groupResourceService.getAllResources();
        if(resources.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/supply/filter")
    public ResponseEntity<List<GroupResource>> getResourcesByFilter(@RequestBody List<String> types){
        List<GroupResource> resources = groupResourceService.getResourcesByType(types);
        if(resources.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/supply/{id}")
    public ResponseEntity<GroupResource> getResource(@PathVariable Long id){
        GroupResource resource = groupResourceService.findResourceById(id);
        if(resource == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping("/newsupply")
    public ResponseEntity<String> addNewResource(@RequestBody ResourceDto resourceDto) {
        try {
            groupResourceService.addNewResource(resourceDto);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Error while adding new resource: " + ex.getMessage()
                                        , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("New resource added succesfully", HttpStatus.CREATED);
    }

    @PostMapping("/newman")
    public ResponseEntity<String> addNewMan(@RequestBody ReactGroupDto reactGroupDto){
        reactGroupService.createNewGroupMember(reactGroupDto);
        return new ResponseEntity<>("New group member added successfully", HttpStatus.CREATED);
    }

    @PostMapping("/criminal/{id}")
    public ResponseEntity<String> appointGroup(@PathVariable Long id, @RequestBody List<Long> peopleIds){
        Criminal criminal = cardService.getCriminalById(id);
        if(criminal == null) {
            return new ResponseEntity<>("Criminal does not exist", HttpStatus.NOT_FOUND);
        }
        if(peopleIds.isEmpty()) {
            return new ResponseEntity<>("React group cannot be empty", HttpStatus.BAD_REQUEST);
        }
        reactGroupService.appointGroupToCriminal(id, peopleIds);
        return new ResponseEntity<>("React group successfully appointed", HttpStatus.OK);
    }

    @PutMapping("/criminal/{id}")
    public ResponseEntity<String> updateCriminalStatus (@PathVariable Long id, @RequestBody String status){
        Criminal criminal = cardService.getCriminalById(id);
        if(criminal == null) {
            return new ResponseEntity<>("Criminal does not exist", HttpStatus.NOT_FOUND);
        }
        CriminalStatus criminalStatus = null;
        if(status == "CAUGHT"){
            criminalStatus = CriminalStatus.CAUGHT;
        }
        else if(status == "ESCAPED") {
            criminalStatus = CriminalStatus.ESCAPED;
        }
        if(criminalStatus == null){
            return new ResponseEntity<>("Unknown status", HttpStatus.BAD_REQUEST);
        }
        cardService.updateCriminalStatus(id, criminalStatus);
        return new ResponseEntity<>("Criminal status successfully updated", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGroupMemberInfo(@PathVariable Long id
                                                , @RequestBody ReactGroupDto reactGroupDto) {
        ReactGroup memberToUpdate = reactGroupService.findGroupMemberById(id);
        if(memberToUpdate == null) {
            return new ResponseEntity<>("Group does not exist", HttpStatus.NOT_FOUND);
        }
        reactGroupService.updateGroupMember(id, reactGroupDto);
        return new ResponseEntity<>("Group successfully updated", HttpStatus.OK);
    }

    @PutMapping("/supply/{id}")
    public ResponseEntity<String> orderResource(@PathVariable Long id, @RequestBody int amount){
        GroupResource resourceToOrder = groupResourceService.findResourceById(id);
        if(resourceToOrder == null){
            return new ResponseEntity<>("Resource does not exist", HttpStatus.NOT_FOUND);
        }
        try {
            groupResourceService.orderResource(id, amount);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Error while order resource: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        // add try-catch construction to handle exception
        return new ResponseEntity<>("Resource successfully ordered", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroupMember (@PathVariable Long id) {
        ReactGroup memberToDelete = reactGroupService.findGroupMemberById(id);
        if(memberToDelete == null) {
            return new ResponseEntity<>("Group member does not exist", HttpStatus.NOT_FOUND);
        }
        reactGroupService.deleteGroupMember(id);
        return new ResponseEntity<>("Group member successfully deleted", HttpStatus.NO_CONTENT);
    }

}
