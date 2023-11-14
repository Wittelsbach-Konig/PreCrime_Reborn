package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.CrimeCardInDto;
import ru.itmo.precrimeupd.dto.CrimeCardOutDto;
import ru.itmo.precrimeupd.dto.CriminalOutDto;
import ru.itmo.precrimeupd.model.CrimeCard;
import ru.itmo.precrimeupd.model.Criminal;
import ru.itmo.precrimeupd.model.CriminalStatus;

import java.util.List;

public interface CardService {
    void createCard(CrimeCardInDto crimeCardInDto);
    void updateCard(Long id , CrimeCardInDto crimeCardInDto);
    void deleteCard(Long id);
    List<CrimeCardOutDto> getAllCards();
    List<CrimeCardOutDto> getAllDetectiveCards();
    CrimeCardOutDto getCardById(Long id);
    CrimeCard findCardById(Long id);
    List<CriminalOutDto> getAllCriminals();
    CriminalOutDto getCriminalById(Long id);
    Criminal findCriminalById(Long id);
    void updateCriminalStatus(Long id, CriminalStatus status);
    void reportCardMistake(Long id, String message);
}
