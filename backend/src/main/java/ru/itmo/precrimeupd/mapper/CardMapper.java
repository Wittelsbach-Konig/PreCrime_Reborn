package ru.itmo.precrimeupd.mapper;

import ru.itmo.precrimeupd.dto.CrimeCardDto;
import ru.itmo.precrimeupd.model.CrimeCard;

public class CardMapper {
    public static CrimeCard mapToCrimeCard(CrimeCardDto cardDto){
        CrimeCard card = CrimeCard.builder()
                .id(cardDto.getId())
                .victimName(cardDto.getVictimName())
                .criminalName(cardDto.getCriminalName())
                .placeOfCrime(cardDto.getPlaceOfCrime())
                .weapon(cardDto.getWeapon())
                .crimeTime(cardDto.getCrimeTime())
                //.responsibleDetective(cardDto.getCreatedBy())
                .build();
        return card;
    }
}
