package ru.itmo.precrimeupd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.PreCogDto;
import ru.itmo.precrimeupd.model.PreCog;
import ru.itmo.precrimeupd.repository.PreCogRepository;
import ru.itmo.precrimeupd.service.PreCogService;

import java.util.List;
import java.util.Optional;

@Service
public class PreCogServiceImpl implements PreCogService {

    private PreCogRepository preCogRepository;

    @Autowired
    public PreCogServiceImpl(PreCogRepository preCogRepository) {
        this.preCogRepository = preCogRepository;
    }

    @Override
    public List<PreCog> getAllPrecogs() {
        List<PreCog> preCogs = preCogRepository.findAll();
        return preCogs;
    }

    @Override
    public PreCog getPrecog(Long id) {
        Optional<PreCog> preCog = preCogRepository.findById(id);
        if(preCog.isPresent()){
            return preCog.get();
        }
        return null;
    }

    @Override
    public void addNewPrecog(PreCogDto preCogDto) {
        PreCog newPreCog = new PreCog();
        newPreCog.setPreCogName(preCogDto.getPreCogName());
        newPreCog.setAge(preCogDto.getAge());
        preCogRepository.save(newPreCog);
    }

    @Override
    public void deletePrecog(Long id) {
        Optional<PreCog> preCogToDelete = preCogRepository.findById(id);
        if (preCogToDelete.isPresent()){
            preCogRepository.deleteById(id);
        }
    }

    @Override
    public void updatePrecodInfo(Long id, PreCogDto preCogDto) {
        PreCog preCogToUpdate = preCogRepository.findById(id).get();
        preCogToUpdate.setPreCogName(preCogDto.getPreCogName());
        preCogToUpdate.setAge(preCogDto.getAge());
        preCogRepository.save(preCogToUpdate);
    }


}
