package ru.itmo.precrimeupd.service;

import ru.itmo.precrimeupd.dto.PreCogDto;
import ru.itmo.precrimeupd.model.PreCog;

import java.util.List;

public interface PreCogService {
    List<PreCog> getAllPrecogs();
    PreCog getPrecog(Long id);
    void addNewPrecog(PreCogDto preCogDto);
    void deletePrecog(Long id);
    void updatePrecodInfo(Long id, PreCogDto preCogDto);

}
