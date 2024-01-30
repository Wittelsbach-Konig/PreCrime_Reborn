package ru.itmo.backend.service;

import ru.itmo.backend.dto.PreCogDto;
import ru.itmo.backend.dto.PreCogOutDto;
import ru.itmo.backend.models.PreCog;

import java.util.List;

public interface PreCogService {
    List<PreCogOutDto> getAllPreCogs();
    PreCog findPreCogById(Long id);
    PreCogOutDto getPreCog(Long id);
    PreCogOutDto addNewPreCog(PreCogDto preCogDto);
    void deletePreCog(Long id);
    PreCogOutDto updatePreCogInfo(Long id, PreCogDto preCogDto);
    PreCogOutDto retirePreCog(Long id);
    PreCogOutDto rehabilitatePreCog(Long id);
    void updateVitalSigns();
    void enterDopamine(Long id, int amount);
    void enterSerotonine(Long id, int amount);
    void enterDepressant(Long id, int amount);
}
