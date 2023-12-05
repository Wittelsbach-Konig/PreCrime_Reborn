package ru.itmo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.backend.dto.*;
import ru.itmo.backend.models.*;
import ru.itmo.backend.service.CardService;
import ru.itmo.backend.service.GroupResourceService;
import ru.itmo.backend.service.ReactGroupService;
import ru.itmo.backend.service.StatisticService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reactiongroup")
@Validated
public class ReactGroupController {
    private final ReactGroupService reactGroupService;
    private final CardService cardService;
    private final GroupResourceService groupResourceService;
    private final StatisticService statisticService;

    @Autowired
    public ReactGroupController(ReactGroupService reactGroupService
                                , CardService cardService
                                , GroupResourceService groupResourceService
                                , StatisticService statisticService) {
        this.reactGroupService = reactGroupService;
        this.cardService = cardService;
        this.groupResourceService = groupResourceService;
        this.statisticService = statisticService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReactGroup>> getAllGroups(){
        List<ReactGroup> reactGroups = reactGroupService.getAllMembers();
        return new ResponseEntity<>(reactGroups, HttpStatus.OK);
    }

    @GetMapping("/allworking")
    public ResponseEntity<List<ReactGroup>> getAllWorkingGroup(){
        List<ReactGroup> workingReactGroup = reactGroupService.getAllWorkingMembers();
        return new ResponseEntity<>(workingReactGroup, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReactGroup> getReactGroup(@PathVariable Long id){
        ReactGroup reactGroup = reactGroupService.findGroupMemberById(id);
        return new ResponseEntity<>(reactGroup, HttpStatus.OK);
    }

    @GetMapping("/{id}/statistic")
    public ResponseEntity<ReactGroupStatisticDto> getMemberStatistic(@PathVariable Long id){
        ReactGroupStatisticDto reactGroupStatisticDto = statisticService.getGroupMemberStatistic(id);
        return new ResponseEntity<>(reactGroupStatisticDto, HttpStatus.OK);
    }

    @GetMapping("/criminal")
    public ResponseEntity<List<CriminalOutDto>> getAllCriminals() {
        List<CriminalOutDto> criminals = cardService.getAllCriminals();
        if(criminals.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(criminals, HttpStatus.OK);
    }

    @GetMapping("/criminal/{id}")
    public ResponseEntity<CriminalOutDto> getCriminal(@PathVariable Long id){
        CriminalOutDto criminal = cardService.getCriminalById(id);
        return new ResponseEntity<>(criminal, HttpStatus.OK);
    }

    @GetMapping("/supply")
    public ResponseEntity<List<GroupResource>> getResources() {
        List<GroupResource> resources = groupResourceService.getAllResources();
        if(resources.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/transport")
    public ResponseEntity<List<Transport>> getAllTransport() {
        List<Transport> transports = groupResourceService.getAllTransport();
        if(transports.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(transports, HttpStatus.OK);
    }

    @GetMapping("/transport/{id}")
    public ResponseEntity<Transport> getTransport(@PathVariable Long id){
        Transport transport = groupResourceService.findTransportById(id);
        return new ResponseEntity<>(transport, HttpStatus.OK);
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
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping("/supply/new")
    public ResponseEntity<ResourceDto> addNewResource(@Valid @RequestBody ResourceDto resourceDto) {
        ResourceDto addedResource = groupResourceService.addNewResource(resourceDto);
        return new ResponseEntity<>(addedResource, HttpStatus.CREATED);
    }

    @PostMapping("/newman")
    public ResponseEntity<ReactGroupOutDto> addNewMan(@Valid @RequestBody ReactGroupInDto reactGroupInDto){
        ReactGroupOutDto newGroupMember = reactGroupService.createNewGroupMember(reactGroupInDto);
        return new ResponseEntity<>(newGroupMember, HttpStatus.CREATED);
    }

    @PostMapping("/transport/new")
    public ResponseEntity<TransportOutDto> addNewTransport(@Valid @RequestBody TransportDto transportDto) {
        TransportOutDto addedTransport = groupResourceService.addNewTransport(transportDto);
        return new ResponseEntity<>(addedTransport, HttpStatus.CREATED);
    }

    @PostMapping(path = "/criminal/{id}", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> appointGroup(@PathVariable Long id, @RequestBody List<Long> peopleIds){
        if(peopleIds.isEmpty()) {
            return new ResponseEntity<>("React group cannot be empty", HttpStatus.BAD_REQUEST);
        }
        reactGroupService.appointGroupToCriminal(id, peopleIds);
        return new ResponseEntity<>("React group successfully appointed", HttpStatus.OK);
    }

    @PutMapping("/criminal/{id}")
    public ResponseEntity<CriminalOutDto> updateCriminalStatus (@PathVariable Long id, @RequestParam String status){
        CriminalStatus criminalStatus = null;
        if(status.equals("CAUGHT")){
            criminalStatus = CriminalStatus.CAUGHT;
        }
        else if(status.equals("ESCAPED")) {
            criminalStatus = CriminalStatus.ESCAPED;
        }
        if(criminalStatus == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CriminalOutDto criminalWithNewStatus = cardService.updateCriminalStatus(id, criminalStatus);
        return new ResponseEntity<>(criminalWithNewStatus, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReactGroupOutDto> updateGroupMemberInfo(@PathVariable Long id
                                                , @Valid @RequestBody ReactGroupInDto reactGroupInDto) {
        ReactGroupOutDto updatedGroupMember = reactGroupService.updateGroupMember(id, reactGroupInDto);
        return new ResponseEntity<>(updatedGroupMember, HttpStatus.OK);
    }

    @PutMapping(path = "/supply/{id}", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> orderResource(@PathVariable Long id, @RequestParam int amount){
        groupResourceService.orderResource(id, amount);
        return new ResponseEntity<>("Resource successfully ordered", HttpStatus.OK);
    }

    @PutMapping("/transport/{id}/retire")
    public ResponseEntity<TransportOutDto> retireTransport(@PathVariable Long id){
        TransportOutDto retiredTransport = groupResourceService.retireTransport(id);
        return new ResponseEntity<>(retiredTransport, HttpStatus.OK);
    }

    @PutMapping("/transport/{id}/rehabilitate")
    public ResponseEntity<TransportOutDto> rehabilitateTransport(@PathVariable Long id){
        TransportOutDto rehabilitatedTransport = groupResourceService.rehabilitateTransport(id);
        return new ResponseEntity<>(rehabilitatedTransport, HttpStatus.OK);
    }

    @PutMapping(path ="/transport/{id}/refuel", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> refuelTransport(@PathVariable Long id, @RequestParam int amount){
        groupResourceService.refuelCar(id, amount);
        return new ResponseEntity<>("Transport refueled successfully",HttpStatus.OK);
    }


//    @DeleteMapping(path ="/{id}", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
//    public ResponseEntity<String> deleteGroupMember (@PathVariable Long id) {
//        reactGroupService.deleteGroupMember(id);
//        return new ResponseEntity<>("Group member successfully deleted", HttpStatus.NO_CONTENT);
//    }
    @PutMapping("/{id}/retire")
    public ResponseEntity<ReactGroupOutDto> retireGroupMember(@PathVariable Long id) {
        ReactGroupOutDto retiredMember = reactGroupService.retireGroupMember(id);
        return new ResponseEntity<>(retiredMember, HttpStatus.OK);
    }

    @DeleteMapping(path = "/transport/{id}", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> deleteTransport (@PathVariable Long id) {
        groupResourceService.deleteTransport(id);
        return new ResponseEntity<>("Transport deleted successfully", HttpStatus.NO_CONTENT);
    }
}
