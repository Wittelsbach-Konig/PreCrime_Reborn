package ru.itmo.precrimeupd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.CrimeCardDto;
import ru.itmo.precrimeupd.model.CrimeCard;
import ru.itmo.precrimeupd.model.CrimeType;
import ru.itmo.precrimeupd.model.UserEntity;
import ru.itmo.precrimeupd.repository.CardRepository;
import ru.itmo.precrimeupd.repository.CrimeTypeRepository;
import ru.itmo.precrimeupd.repository.UserRepository;
import ru.itmo.precrimeupd.security.SecurityUtil;
import ru.itmo.precrimeupd.service.CardService;

import java.util.List;
import java.util.Optional;

import static ru.itmo.precrimeupd.mapper.CardMapper.mapToCrimeCard;

@Service
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;
    private CrimeTypeRepository crimeTypeRepository;
    private UserRepository userRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, CrimeTypeRepository crimeTypeRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.crimeTypeRepository = crimeTypeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createCard(CrimeCardDto crimeCardDto) {
        String login = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        CrimeCard crimeCard = mapToCrimeCard(crimeCardDto);
        crimeCard.setResponsibleDetective(user);
        CrimeType crimeType = crimeTypeRepository.findByName(crimeCardDto.getCrimeType());
        if(crimeType != null){
            crimeCard.setTypeOfCrime(crimeType);
        }
        cardRepository.save(crimeCard);
    }

    @Override
    public void updateCard(Long id, CrimeCardDto crimeCardDto) {

        CrimeCard cardToUpdate = cardRepository.findById(id).get();
        cardToUpdate.setVictimName(crimeCardDto.getVictimName());
        cardToUpdate.setCriminalName(crimeCardDto.getCriminalName());
        cardToUpdate.setPlaceOfCrime(crimeCardDto.getPlaceOfCrime());
        cardToUpdate.setWeapon(crimeCardDto.getWeapon());
        cardToUpdate.setCrimeTime(crimeCardDto.getCrimeTime());
        CrimeType crimeType = crimeTypeRepository.findByName(crimeCardDto.getCrimeType());
        if(crimeType != null){
            cardToUpdate.setTypeOfCrime(crimeType);
        }
        cardRepository.save(cardToUpdate);
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
}
