package ru.itmo.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.backend.dto.CrimeCardInDto;
import ru.itmo.backend.dto.CrimeCardOutDto;
import ru.itmo.backend.dto.CriminalOutDto;
import ru.itmo.backend.exceptions.NotFoundException;
import ru.itmo.backend.exceptions.NotValidArgumentException;
import ru.itmo.backend.models.*;
import ru.itmo.backend.repository.*;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.CardService;
import ru.itmo.backend.service.PreCogService;
import ru.itmo.backend.service.StatisticService;
import ru.itmo.backend.service.TelegramBotService;
//import ru.itmo.precrimesyst.service.TelegramBotService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static ru.itmo.backend.mapper.CardMapper.mapToCrimeCard;
import static ru.itmo.backend.mapper.CardMapper.mapToCrimeCardOutDto;
import static ru.itmo.backend.mapper.CriminalOutMapper.mapToCriminalOutDto;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CriminalRepository criminalRepository;
    private final UserRepository userRepository;
    private final TelegramBotService telegramBotService;
    private final StatisticService statisticService;
    private final PreCogService preCogService;
    private final VisionRepository visionRepository;
    private final SecurityUtil securityUtil;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository
                            , UserRepository userRepository
                            , CriminalRepository criminalRepository
                            , TelegramBotService telegramBotService
                            , StatisticService statisticService
                            , PreCogService preCogService
                            , VisionRepository visionRepository

                            , SecurityUtil securityUtil) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.criminalRepository = criminalRepository;
        this.telegramBotService = telegramBotService;
        this.statisticService = statisticService;
        this.preCogService = preCogService;
        this.visionRepository = visionRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public CrimeCardOutDto createCard(CrimeCardInDto crimeCardInDto) {
        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        CrimeCard crimeCard = mapToCrimeCard(crimeCardInDto);
        crimeCard.setResponsibleDetective(user);
        if(crimeCardInDto.getCrimeType().equals("INTENTIONAL")){
            crimeCard.setTypeOfCrime(CrimeType.INTENTIONAL);
        }
        if(crimeCardInDto.getCrimeType().equals("UNINTENTIONAL")) {
            crimeCard.setTypeOfCrime(CrimeType.UNINTENTIONAL);
        }
        Vision crimeVision = visionRepository.findById(crimeCardInDto.getVisionId())
                .orElseThrow(() -> new NoSuchElementException("Vision not found"));
        if (!crimeVision.isAccepted()) {
            throw new NotValidArgumentException("Vision " + crimeVision.getId() + "is not accepted! Please contact to your technic.");
        }
        crimeCard.setVision(crimeVision);
        Criminal criminal = new Criminal();
        CrimeCard newCard = cardRepository.save(crimeCard);
        CrimeCardOutDto resultCard = mapToCrimeCardOutDto(newCard);
        statisticService.completedInvestigation(crimeCard.getResponsibleDetective());
        preCogService.updateVitalSigns();
        criminal.setLocation(crimeCardInDto.getPlaceOfCrime());
        criminal.setName(crimeCardInDto.getCriminalName());
        criminal.setWeapon(crimeCardInDto.getWeapon());
        criminal.setStatus(CriminalStatus.NOT_CAUGHT);
        criminal.setCrimeCard(crimeCard);
        criminalRepository.save(criminal);
        return resultCard;
    }

    @Override
    public CrimeCardOutDto updateCard(Long id, CrimeCardInDto crimeCardInDto) {
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
        CrimeCard savedCard = cardRepository.save(cardToUpdate);
        CrimeCardOutDto resultCard = mapToCrimeCardOutDto(savedCard);
        criminalToUpdate.setName(crimeCardInDto.getCriminalName());
        criminalToUpdate.setWeapon(crimeCardInDto.getWeapon());
        criminalToUpdate.setLocation(crimeCardInDto.getPlaceOfCrime());
        criminalRepository.save(criminalToUpdate);
        return resultCard;
    }

    @Override
    public void deleteCard(Long id)  {
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
        String login = securityUtil.getSessionUser();
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
    public CrimeCardOutDto getCardById(Long id)  {
        CrimeCard card = findCardById(id);
        return prepareCardForOutput(card);
    }

    @Override
    public CrimeCard findCardById(Long id)  {
        return cardRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Card not found: " + id));
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
        Optional<Criminal> criminalOpt = criminalRepository.findById(id);
        if(criminalOpt.isPresent()) {
            Criminal criminal =  criminalOpt.get();
            CriminalOutDto criminalOutDto = prepareCriminalForOutput(criminal);
            return criminalOutDto;
        }
        return null;
    }

    @Override
    public Criminal findCriminalById(Long id) {
        return criminalRepository.findById(id).orElseThrow(()-> new NotFoundException("Criminal not found: " + id));
    }

    @Override
    public CriminalOutDto updateCriminalStatus(Long id, CriminalStatus status) {
        Criminal criminal = findCriminalById(id);
        switch (status) {
            case CAUGHT:
                CrimeCard cardToUpdate = criminal.getCrimeCard();
                cardToUpdate.setIsCriminalCaught(true);
                cardRepository.save(cardToUpdate);
                criminal.setStatus(CriminalStatus.CAUGHT);
                statisticService.criminalArrested(criminal);
                break;
            case ESCAPED:
                criminal.setStatus(CriminalStatus.ESCAPED);
                statisticService.criminalEscaped(criminal);
                break;
            case NOT_CAUGHT:
                criminal.setStatus(CriminalStatus.NOT_CAUGHT);
                break;
            default:
                break;
        }
        Criminal updatedCriminal = criminalRepository.save(criminal);
        return mapToCriminalOutDto(updatedCriminal);
    }

    @Override
    public void reportCardMistake(Long id, String message) {
        CrimeCard card = findCardById(id);
        String informMessage = "The auditor found an error in the card №"
                + id +
                "\nMake corrections in accordance with the auditor’s recommendations\n"
                + message;
        UserEntity detectiveToInform = card.getResponsibleDetective();
        telegramBotService.sendMessage(detectiveToInform.getTelegramId(), informMessage);
        statisticService.foundErrorInCard(detectiveToInform);
    }

    private CrimeCardOutDto prepareCardForOutput(CrimeCard card){
        if(card != null) {
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
