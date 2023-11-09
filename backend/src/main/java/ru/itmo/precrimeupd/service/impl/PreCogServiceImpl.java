package ru.itmo.precrimeupd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.precrimeupd.dto.PreCogDto;
import ru.itmo.precrimeupd.model.GroupResource;
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
    public List<PreCog> getAllPreCogs() {
        List<PreCog> preCogs = preCogRepository.findAll();
        return preCogs;
    }

    @Override
    public PreCog getPreCog(Long id) {
        Optional<PreCog> preCog = preCogRepository.findById(id);
        if(preCog.isPresent()){
            return preCog.get();
        }
        return null;
    }

    @Override
    public void addNewPreCog(PreCogDto preCogDto) {
        PreCog newPreCog = new PreCog();
        newPreCog.setPreCogName(preCogDto.getPreCogName());
        newPreCog.setAge(preCogDto.getAge());
        preCogRepository.save(newPreCog);
    }

    @Override
    public void deletePreCog(Long id) {
        Optional<PreCog> preCogToDelete = preCogRepository.findById(id);
        if (preCogToDelete.isPresent()){
            preCogRepository.deleteById(id);
        }
    }

    @Override
    public void updatePreCogInfo(Long id, PreCogDto preCogDto) {
        PreCog preCogToUpdate = preCogRepository.findById(id).get();
        preCogToUpdate.setPreCogName(preCogDto.getPreCogName());
        preCogToUpdate.setAge(preCogDto.getAge());
        preCogRepository.save(preCogToUpdate);
    }

    @Override
    public void retirePreCog(Long id) {
        PreCog preCogToRetire = preCogRepository.findById(id).get();
        preCogToRetire.setWork(false);
        preCogRepository.save(preCogToRetire);
    }

    @Override
    public void rehabilitatePreCog(Long id) {
        PreCog preCogToRetire = preCogRepository.findById(id).get();
        preCogToRetire.setWork(true);
        preCogRepository.save(preCogToRetire);
    }

    @Override
    public void enterDopamine(Long id, int amount) throws IllegalArgumentException {
        PreCog preCogToService = preCogRepository.findById(id).get();
        int maxPossibleAmount = 100;
        int currentAmount = preCogToService.getDopamineLevel();
        int newAmount = currentAmount + amount;
        if(newAmount > maxPossibleAmount){
            throw new IllegalArgumentException("Too much dopamine");
        }
        preCogToService.setDopamineLevel(newAmount);
        preCogRepository.save(preCogToService);
    }

    @Override
    public void enterSerotonine(Long id, int amount) throws IllegalArgumentException {
        PreCog preCogToService = preCogRepository.findById(id).get();
        int maxPossibleAmount = 100;
        int currentAmount = preCogToService.getSerotoninLevel();
        int newAmount = currentAmount + amount;
        if(newAmount > maxPossibleAmount){
            throw new IllegalArgumentException("Too much serotonine");
        }
        preCogToService.setSerotoninLevel(newAmount);
        preCogRepository.save(preCogToService);
    }

    @Override
    public void enterDepressant(Long id, int amount) throws IllegalArgumentException {
        PreCog preCogToService = preCogRepository.findById(id).get();
        int minPossibleAmount = 0;
        int currentAmount = preCogToService.getStressLevel();
        int newAmount = currentAmount - amount;
        if(newAmount < minPossibleAmount){
            throw new IllegalArgumentException("Too much depressants");
        }
        preCogToService.setDopamineLevel(newAmount);
        preCogRepository.save(preCogToService);
    }
}
