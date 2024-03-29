package ru.itmo.backend.mapper;

import ru.itmo.backend.dto.CrimeCardInDto;
import ru.itmo.backend.dto.CrimeCardOutDto;
import ru.itmo.backend.models.CrimeCard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CardMapper {
    public static CrimeCard mapToCrimeCard(CrimeCardInDto cardDto){
        return CrimeCard.builder()
                .id(cardDto.getId())
                .victimName(cardDto.getVictimName())
                .criminalName(cardDto.getCriminalName())
                .placeOfCrime(cardDto.getPlaceOfCrime())
                .weapon(cardDto.getWeapon())
                .crimeTime(cardDto.getCrimeTime())
                .build();
    }

    public static CrimeCardOutDto mapToCrimeCardOutDto(CrimeCard crimeCard) {
        return CrimeCardOutDto.builder()
                .id(crimeCard.getId())
                .crimeTime(convertDateToString(crimeCard.getCrimeTime()))
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
                .creationDate(convertDateToString(crimeCard.getCreationDate()))
                .build();
    }

    private static String convertDateToString(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }
}
