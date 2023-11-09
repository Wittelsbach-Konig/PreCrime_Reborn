package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.PreCogDto;
import ru.itmo.precrimeupd.model.PreCog;

import java.util.List;

public interface PreCogService {
    List<PreCog> getAllPreCogs();
    PreCog getPreCog(Long id);
    void addNewPreCog(PreCogDto preCogDto);
    void deletePreCog(Long id);
    void updatePreCogInfo(Long id, PreCogDto preCogDto);
    void retirePreCog(Long id);
    void rehabilitatePreCog(Long id);
    void enterDopamine(Long id, int amount) throws IllegalArgumentException;
    void enterSerotonine(Long id, int amount) throws IllegalArgumentException;
    void enterDepressant(Long id, int amount) throws IllegalArgumentException;
}
