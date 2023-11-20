package ru.itmo.backend.service;

import ru.itmo.backend.dto.PreCogDto;
import ru.itmo.backend.models.PreCog;

import java.util.List;

public interface PreCogService {
    List<PreCog> getAllPreCogs();
    PreCog findPreCogById(Long id);
    PreCog getPreCog(Long id);
    PreCog addNewPreCog(PreCogDto preCogDto);
    void deletePreCog(Long id);
    PreCog updatePreCogInfo(Long id, PreCogDto preCogDto);
    PreCog retirePreCog(Long id);
    PreCog rehabilitatePreCog(Long id);
    void updateVitalSigns();
    void enterDopamine(Long id, int amount);
    void enterSerotonine(Long id, int amount);
    void enterDepressant(Long id, int amount);
}
