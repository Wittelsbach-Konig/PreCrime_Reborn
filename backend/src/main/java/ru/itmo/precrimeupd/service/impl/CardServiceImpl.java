package ru.itmo.precrimeupd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.CrimeCardInDto;
import ru.itmo.precrimeupd.dto.CrimeCardOutDto;
import ru.itmo.precrimeupd.dto.CriminalOutDto;
import ru.itmo.precrimeupd.exceptions.NotFoundException;
import ru.itmo.precrimeupd.model.*;
import ru.itmo.precrimeupd.repository.*;
import ru.itmo.precrimeupd.security.SecurityUtil;
import ru.itmo.precrimeupd.service.CardService;
import ru.itmo.precrimeupd.service.PreCogService;
import ru.itmo.precrimeupd.service.StatisticService;
import ru.itmo.precrimeupd.service.TelegramBotService;

import java.util.ArrayList;
import java.util.List;

import static ru.itmo.precrimeupd.mapper.CardMapper.mapToCrimeCard;
import static ru.itmo.precrimeupd.mapper.CardMapper.mapToCrimeCardOutDto;
import static ru.itmo.precrimeupd.mapper.CriminalOutMapper.mapToCriminalOutDto;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CriminalRepository criminalRepository;
    private final UserRepository userRepository;
    private final TelegramBotService telegramBotService;
    private final StatisticService statisticService;
    private final PreCogService preCogService;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository
                            , UserRepository userRepository
                            , CriminalRepository criminalRepository
                            , TelegramBotService telegramBotService
                            , StatisticService statisticService
                            , PreCogService preCogService) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.criminalRepository = criminalRepository;
        this.telegramBotService = telegramBotService;
        this.statisticService = statisticService;
        this.preCogService = preCogService;
    }

    @Override
    public void createCard(CrimeCardInDto crimeCardInDto) {
        String login = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        CrimeCard crimeCard = mapToCrimeCard(crimeCardInDto);
        crimeCard.setResponsibleDetective(user);
        if(crimeCardInDto.getCrimeType().equals("INTENTIONAL")){
            crimeCard.setTypeOfCrime(CrimeType.INTENTIONAL);
        }
        if(crimeCardInDto.getCrimeType().equals("UNINTENTIONAL")) {
            crimeCard.setTypeOfCrime(CrimeType.UNINTENTIONAL);
        }
        Criminal criminal = new Criminal();
        cardRepository.save(crimeCard);
        statisticService.completedInvestigation(crimeCard.getResponsibleDetective());
        preCogService.updateVitalSigns();
        criminal.setLocation(crimeCardInDto.getPlaceOfCrime());
        criminal.setName(crimeCardInDto.getCriminalName());
        criminal.setWeapon(crimeCardInDto.getWeapon());
        criminal.setStatus(CriminalStatus.NOT_CAUGHT);
        criminal.setCrimeCard(crimeCard);
        criminalRepository.save(criminal);
    }

    @Override
    public void updateCard(Long id, CrimeCardInDto crimeCardInDto) {
        CrimeCard cardToUpdate = findCardById(id);
        Criminal criminalToUpdate = criminalRepository.findByCrimeCard(cardToUpdate);
        cardToUpdate.setVictimName(crimeCardInDto.getVictimName());
        cardToUpdate.setCriminalName(crimeCardInDto.getCriminalName());
        cardToUpdate.setPlaceOfCrime(crimeCardInDto.getPlaceOfCrime());
        cardToUpdate.setWeapon(crimeCardInDto.getWeapon());
        cardToUpdate.setCrimeTime(crimeCardInDto.getCrimeTime());
        if (crimeCardInDto.getCrimeType().equals("INTENTIONAL")) {
            cardToUpdate.setTypeOfCrime(CrimeType.INTENTIONAL);
        }
        if (crimeCardInDto.getCrimeType().equals("UNINTENTIONAL")) {
            cardToUpdate.setTypeOfCrime(CrimeType.UNINTENTIONAL);
        }
        cardRepository.save(cardToUpdate);
        criminalToUpdate.setName(crimeCardInDto.getCriminalName());
        criminalToUpdate.setWeapon(crimeCardInDto.getWeapon());
        criminalToUpdate.setLocation(crimeCardInDto.getPlaceOfCrime());
    }

    @Override
    public void deleteCard(Long id) {
        CrimeCard cardToDelete = findCardById(id);
        cardRepository.delete(cardToDelete);
    }

    @Override
    public List<CrimeCardOutDto> getAllCards() {
        List<CrimeCard> crimeCards = cardRepository.findAll();
        List<CrimeCardOutDto> crimeCardOutDtos = new ArrayList<>();
        for(CrimeCard card : crimeCards){
            CrimeCardOutDto crimeCardOutDto = prepareCardForOutput(card);
            crimeCardOutDtos.add(crimeCardOutDto);
        }
        return crimeCardOutDtos;
    }

    @Override
    public List<CrimeCardOutDto> getAllDetectiveCards() {
        String login = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        List<CrimeCard> crimeCards = cardRepository.findAllByResponsibleDetective(user);
        List<CrimeCardOutDto> crimeCardOutDtos = new ArrayList<>();
        for(CrimeCard card : crimeCards){
            CrimeCardOutDto crimeCardOutDto = prepareCardForOutput(card);
            crimeCardOutDtos.add(crimeCardOutDto);
        }
        return crimeCardOutDtos;
    }

    @Override
    public CrimeCardOutDto getCardById(Long id) {
        CrimeCard crimeCard = findCardById(id);
        return prepareCardForOutput(crimeCard);
    }

    @Override
    public CrimeCard findCardById(Long id) {
        return cardRepository.findById(id).orElseThrow(()->new NotFoundException("Card not found: " + id));
    }

    @Override
    public List<CriminalOutDto> getAllCriminals() {
        List<Criminal> criminals = criminalRepository.findAllByStatusIsLike(CriminalStatus.NOT_CAUGHT);
        List<CriminalOutDto> criminalOutDtos = new ArrayList<>();
        for(Criminal criminal : criminals){
            CriminalOutDto criminalOutDto = prepareCriminalForOutput(criminal);
            criminalOutDtos.add(criminalOutDto);
        }
        return criminalOutDtos;
    }

    @Override
    public CriminalOutDto getCriminalById(Long id) {
        Criminal criminal = findCriminalById(id);
        return prepareCriminalForOutput(criminal);
    }

    @Override
    public Criminal findCriminalById(Long id) {
        return criminalRepository.findById(id).orElseThrow(()-> new NotFoundException("Criminal not found: " + id));
    }

    @Override
    public void updateCriminalStatus(Long id, CriminalStatus status) {
        Criminal criminal = findCriminalById(id);
        if(status == CriminalStatus.ESCAPED){
            criminal.setStatus(CriminalStatus.ESCAPED);
            statisticService.criminalEscaped(criminal);
        }
        else if(status == CriminalStatus.CAUGHT) {
            CrimeCard cardToUpdate = criminal.getCrimeCard();
            cardToUpdate.setIsCriminalCaught(true);
            cardRepository.save(cardToUpdate);
            criminal.setStatus(CriminalStatus.CAUGHT);
            statisticService.criminalArrested(criminal);
        }
        criminalRepository.save(criminal);
    }

    @Override
    public void reportCardMistake(Long id, String message) {
        CrimeCard card = findCardById(id);
        String informMessage = "the auditor found an error in the card №"
                + id +
                "\nMake corrections in accordance with the auditor’s recommendations\n"
                + message;
        UserEntity detectiveToInform = card.getResponsibleDetective();
        telegramBotService.sendMessage(detectiveToInform.getTelegramId(), informMessage);
        statisticService.foundErrorInCard(detectiveToInform);
    }

    private CrimeCardOutDto prepareCardForOutput(CrimeCard card){
        if(card != null){
            return mapToCrimeCardOutDto(card);
        }
        return null;
    }

    private CriminalOutDto prepareCriminalForOutput(Criminal criminal){
        if(criminal != null){
            return mapToCriminalOutDto(criminal);
        }
        return null;
    }
}
