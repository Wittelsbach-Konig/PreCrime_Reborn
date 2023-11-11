package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.CrimeCardDto;
import ru.itmo.precrimeupd.model.CrimeCard;
import ru.itmo.precrimeupd.model.Criminal;
import ru.itmo.precrimeupd.model.CriminalStatus;

import java.util.List;

public interface CardService {
    void createCard(CrimeCardDto crimeCardDto);
    void updateCard(Long id ,CrimeCardDto crimeCardDto);
    void deleteCard(Long id);
    List<CrimeCard> getAllCards();
    CrimeCard getCardById(Long id);
    List<Criminal> getAllCriminals();
    Criminal getCriminalById(Long id);
    void updateCriminalStatus(Long id, CriminalStatus status);
    void reportCardMistake(Long id, String messsage);
}
