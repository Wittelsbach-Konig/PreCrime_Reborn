package ru.itmo.backend.service;

import ru.itmo.backend.dto.CrimeCardInDto;
import ru.itmo.backend.dto.CrimeCardOutDto;
import ru.itmo.backend.dto.CriminalOutDto;
import ru.itmo.backend.models.CrimeCard;
import ru.itmo.backend.models.Criminal;
import ru.itmo.backend.models.CriminalStatus;

import java.util.List;

public interface CardService {
    CrimeCardOutDto createCard(CrimeCardInDto crimeCardInDto);
    CrimeCardOutDto updateCard(Long id , CrimeCardInDto crimeCardInDto);
    void deleteCard(Long id);
    List<CrimeCardOutDto> getAllCards();
    List<CrimeCardOutDto> getAllDetectiveCards();
    CrimeCardOutDto getCardById(Long id);
    CrimeCard findCardById(Long id);
    List<CriminalOutDto> getAllCriminals();
    CriminalOutDto getCriminalById(Long id);
    Criminal findCriminalById(Long id);
    CriminalOutDto updateCriminalStatus(Long id, CriminalStatus status);
    void reportCardMistake(Long id, String message);
}
