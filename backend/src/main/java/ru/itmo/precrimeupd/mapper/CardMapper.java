package ru.itmo.precrimeupd.mapper;

import ru.itmo.precrimeupd.dto.CrimeCardInDto;
import ru.itmo.precrimeupd.dto.CrimeCardOutDto;
import ru.itmo.precrimeupd.model.CrimeCard;

public class CardMapper {
    public static CrimeCard mapToCrimeCard(CrimeCardInDto cardDto){
        CrimeCard card = CrimeCard.builder()
                .id(cardDto.getId())
                .victimName(cardDto.getVictimName())
                .criminalName(cardDto.getCriminalName())
                .placeOfCrime(cardDto.getPlaceOfCrime())
                .weapon(cardDto.getWeapon())
                .crimeTime(cardDto.getCrimeTime())
                .build();
        return card;
    }

    public static CrimeCardOutDto mapToCrimeCardOutDto(CrimeCard crimeCard) {
        CrimeCardOutDto card = CrimeCardOutDto.builder()
                .id(crimeCard.getId())
                .crimeTime(crimeCard.getCrimeTime())
                .placeOfCrime(crimeCard.getPlaceOfCrime())
                .criminalName(crimeCard.getCriminalName())
                .victimName(crimeCard.getVictimName())
                .weapon(crimeCard.getWeapon())
                .crimeType(crimeCard.getTypeOfCrime().name())
                .responsibleDetective(crimeCard.getResponsibleDetective().getFirstName()
                                    + " "
                                    + crimeCard.getResponsibleDetective().getLastName())
                .isCriminalCaught(crimeCard.getIsCriminalCaught())
                .visionUrl(crimeCard.getVision().getVideoUrl())
                .build();
        return card;
    }
}
