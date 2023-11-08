package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.CrimeCardDto;
import ru.itmo.precrimeupd.model.CrimeCard;

import java.util.List;

public interface CardService {
    void createCard(CrimeCardDto crimeCardDto);
    void updateCard(Long id ,CrimeCardDto crimeCardDto);
    void deleteCard(Long id);
    List<CrimeCard> getAllCards();
    CrimeCard getCardById(Long id);
}
