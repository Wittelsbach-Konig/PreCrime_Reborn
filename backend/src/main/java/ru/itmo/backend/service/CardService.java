package ru.itmo.backend.service;

import ru.itmo.backend.dto.CrimeCardInDto;
import ru.itmo.backend.dto.CrimeCardOutDto;
import ru.itmo.backend.dto.CrimeCardUpdateDto;
import ru.itmo.backend.dto.CriminalOutDto;
import ru.itmo.backend.models.CrimeCard;
import ru.itmo.backend.models.Criminal;
import ru.itmo.backend.models.CriminalStatus;

import java.util.List;

public interface CardService {
    CrimeCardOutDto createCard(CrimeCardInDto crimeCardInDto);
    CrimeCardOutDto updateCard(Long id , CrimeCardUpdateDto crimeCardInDto);
    void deleteCard(Long id);
    List<CrimeCardOutDto> getAllCards();
    List<CrimeCardOutDto> getAllCardsSortedByCrimeTime(String direction);
    List<CrimeCardOutDto> getAllCardsSortedByCreationDate(String direction);
    List<CrimeCardOutDto> getAllDetectiveCards();
    List<CrimeCardOutDto> getAllDetectiveCardsSortedByCrimeTime(String direction);
    List<CrimeCardOutDto> getAllDetectiveCardsSortedByCreationDate(String direction);
    CrimeCardOutDto getCardById(Long id);
    CrimeCard findCardById(Long id);
    List<CriminalOutDto> getAllCriminals(CriminalStatus status);
    CriminalOutDto getCriminalById(Long id);
    Criminal findCriminalById(Long id);
    CriminalOutDto updateCriminalStatus(Long id, CriminalStatus status);
    void reportCardMistake(Long id, String message);
}
