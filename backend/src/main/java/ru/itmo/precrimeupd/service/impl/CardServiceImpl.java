package ru.itmo.precrimeupd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.CrimeCardDto;
import ru.itmo.precrimeupd.model.*;
import ru.itmo.precrimeupd.repository.*;
import ru.itmo.precrimeupd.security.SecurityUtil;
import ru.itmo.precrimeupd.service.CardService;
import ru.itmo.precrimeupd.service.PreCogService;
import ru.itmo.precrimeupd.service.StatisticService;
import ru.itmo.precrimeupd.service.TelegramBotService;

import java.util.List;
import java.util.Optional;

import static ru.itmo.precrimeupd.mapper.CardMapper.mapToCrimeCard;

@Service
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;
    private CriminalRepository criminalRepository;
    private UserRepository userRepository;
    private TelegramBotService telegramBotService;
    private StatisticService statisticService;
    private PreCogService preCogService;

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
    public void createCard(CrimeCardDto crimeCardDto) {
        String login = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        CrimeCard crimeCard = mapToCrimeCard(crimeCardDto);
        crimeCard.setResponsibleDetective(user);
        if(crimeCardDto.getCrimeType() == "INTENTIONAL"){
            crimeCard.setTypeOfCrime(CrimeType.INTENTIONAL);
        }
        if(crimeCardDto.getCrimeType() == "UNINTENTIONAL") {
            crimeCard.setTypeOfCrime(CrimeType.UNINTENTIONAL);
        }
        Criminal criminal = new Criminal();
        cardRepository.save(crimeCard);
        statisticService.completedInvestigation(crimeCard.getResponsibleDetective());
        preCogService.updateVitalSigns();
        criminal.setLocation(crimeCardDto.getPlaceOfCrime());
        criminal.setName(crimeCardDto.getCriminalName());
        criminal.setWeapon(crimeCardDto.getWeapon());
        criminal.setStatus(CriminalStatus.NOT_CAUGHT);
        criminal.setCrimeCard(crimeCard);
        criminalRepository.save(criminal);
    }

    @Override
    public void updateCard(Long id, CrimeCardDto crimeCardDto) {

        CrimeCard cardToUpdate = cardRepository.findById(id).get();
        Criminal criminalToUpdate = criminalRepository.findByCrimeCard(cardToUpdate);
        cardToUpdate.setVictimName(crimeCardDto.getVictimName());
        cardToUpdate.setCriminalName(crimeCardDto.getCriminalName());
        cardToUpdate.setPlaceOfCrime(crimeCardDto.getPlaceOfCrime());
        cardToUpdate.setWeapon(crimeCardDto.getWeapon());
        cardToUpdate.setCrimeTime(crimeCardDto.getCrimeTime());
        if (crimeCardDto.getCrimeType() == "INTENTIONAL") {
            cardToUpdate.setTypeOfCrime(CrimeType.INTENTIONAL);
        }
        if (crimeCardDto.getCrimeType() == "UNINTENTIONAL") {
            cardToUpdate.setTypeOfCrime(CrimeType.UNINTENTIONAL);
        }
        cardRepository.save(cardToUpdate);
        criminalToUpdate.setName(crimeCardDto.getCriminalName());
        criminalToUpdate.setWeapon(crimeCardDto.getWeapon());
        criminalToUpdate.setLocation(crimeCardDto.getPlaceOfCrime());
    }

    @Override
    public void deleteCard(Long id) {
        Optional<CrimeCard> cardToDelete = cardRepository.findById(id);
        if(cardToDelete.isPresent()){
            cardRepository.deleteById(id);
        }
    }

    @Override
    public List<CrimeCard> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public CrimeCard getCardById(Long id) {
        Optional<CrimeCard> card = cardRepository.findById(id);
        if(card.isPresent()){
            return card.get();
        }
        return null;
    }

    @Override
    public List<Criminal> getAllCriminals() {
        return criminalRepository.findByStatusIsLike(CriminalStatus.NOT_CAUGHT);
    }

    @Override
    public Criminal getCriminalById(Long id) {
        Optional<Criminal> criminal = criminalRepository.findById(id);
        if(criminal.isPresent()) {
            return criminal.get();
        }
        return null;
    }

    @Override
    public void updateCriminalStatus(Long id, CriminalStatus status) {
        Criminal criminal = criminalRepository.findById(id).get();
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
        CrimeCard card = cardRepository.findById(id).get();
        String informMessage = "the auditor found an error in the card №"
                + id +
                "\nMake corrections in accordance with the auditor’s recommendations\n"
                + message;
        UserEntity detectiveToInform = card.getResponsibleDetective();
        telegramBotService.sendMessage(detectiveToInform.getTelegramId(), informMessage);
        statisticService.foundErrorInCard(detectiveToInform);
    }
}
