package ru.itmo.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.backend.dto.PreCogDto;
import ru.itmo.backend.dto.PreCogOutDto;
import ru.itmo.backend.exceptions.NotFoundException;
import ru.itmo.backend.exceptions.NotValidArgumentException;
import ru.itmo.backend.models.PreCog;
import ru.itmo.backend.models.UserEntity;
import ru.itmo.backend.repository.PreCogRepository;
import ru.itmo.backend.repository.UserRepository;
import ru.itmo.backend.security.SecurityUtil;
import ru.itmo.backend.service.PreCogService;
import ru.itmo.backend.service.StatisticService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.itmo.backend.mapper.PreCogMapper.mapToPreCogOutDto;

@Service
public class PreCogServiceImpl implements PreCogService {

    private final PreCogRepository preCogRepository;
    private final UserRepository userRepository;
    private final StatisticService statisticService;
    private final SecurityUtil securityUtil;

    @Autowired
    public PreCogServiceImpl(PreCogRepository preCogRepository
            , UserRepository userRepository
            , StatisticService statisticService
            , SecurityUtil securityUtil) {
        this.preCogRepository = preCogRepository;
        this.userRepository = userRepository;
        this.statisticService = statisticService;
        this.securityUtil = securityUtil;
    }

    @Override
    public List<PreCogOutDto> getAllPreCogs() {
        List<PreCog> preCogs = preCogRepository.findAll();
        List<PreCogOutDto> result = new ArrayList<>();
        for(PreCog preCog : preCogs) {
            result.add(mapToPreCogOutDto(preCog));
        }
        return result;
    }

    @Override
    public PreCog findPreCogById(Long id) {
        return preCogRepository.findById(id).orElseThrow(()-> new NotFoundException("PreCog not found:" + id));
    }

    @Override
    public PreCogOutDto getPreCog(Long id) {
        PreCog preCog = findPreCogById(id);
        return mapToPreCogOutDto(preCog);
    }

    @Override
    public PreCogOutDto addNewPreCog(PreCogDto preCogDto) {
        PreCog newPreCog = new PreCog();
        newPreCog.setPreCogName(preCogDto.getPreCogName());
        newPreCog.setAge(preCogDto.getAge());
        PreCog savedPreCog = preCogRepository.save(newPreCog);
        return mapToPreCogOutDto(savedPreCog);
    }

    @Override
    public void deletePreCog(Long id) {
        PreCog preCogToDelete = findPreCogById(id);
        preCogRepository.delete(preCogToDelete);
    }

    @Override
    public PreCogOutDto updatePreCogInfo(Long id, PreCogDto preCogDto) {
        PreCog preCogToUpdate = findPreCogById(id);
        preCogToUpdate.setPreCogName(preCogDto.getPreCogName());
        preCogToUpdate.setAge(preCogDto.getAge());
        PreCog updatedPreCog = preCogRepository.save(preCogToUpdate);
        return mapToPreCogOutDto(updatedPreCog);
    }

    @Override
    public PreCogOutDto retirePreCog(Long id) {
        PreCog preCogToRetire = findPreCogById(id);
        preCogToRetire.setWork(false);
        PreCog updatedPreCog = preCogRepository.save(preCogToRetire);
        return mapToPreCogOutDto(updatedPreCog);
    }

    @Override
    public PreCogOutDto rehabilitatePreCog(Long id) {
        PreCog preCogToRetire = findPreCogById(id);
        preCogToRetire.setWork(true);
        PreCog updatedPreCog = preCogRepository.save(preCogToRetire);
        return mapToPreCogOutDto(updatedPreCog);
    }

    @Override
    public void updateVitalSigns() {
        List<PreCog> preCogs = preCogRepository.findAllByIsWorkTrue();
        if(!preCogs.isEmpty()){
            Random random = new Random();
            for(PreCog preCog : preCogs){
                int dopamineChange = random.nextInt(15) + 1;
                int serotoninChange = random.nextInt(15) + 1;
                int stressChange = random.nextInt(15) + 1;
                preCog.setSerotoninLevel(Math.max(0, preCog.getSerotoninLevel() - serotoninChange));
                preCog.setDopamineLevel(Math.max(0, preCog.getDopamineLevel() - dopamineChange));
                preCog.setStressLevel(Math.min(100, preCog.getStressLevel() + stressChange));
                preCogRepository.save(preCog);
                if(preCog.getDopamineLevel() == 0
                        || preCog.getSerotoninLevel() == 0
                        || preCog.getStressLevel() == 100) {
                    retirePreCog(preCog.getId());
                }
            }
        }
    }

    @Override
    public void enterDopamine(Long id, int amount) {
        PreCog preCogToService = findPreCogById(id);
        int maxPossibleAmount = 100;
        int currentAmount = preCogToService.getDopamineLevel();
        int newAmount = currentAmount + amount;
        if(newAmount > maxPossibleAmount){
            throw new NotValidArgumentException("Too much dopamine. Maximum possible amount is: " + maxPossibleAmount);
        }
        preCogToService.setDopamineLevel(newAmount);
        preCogRepository.save(preCogToService);
        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        statisticService.enteredDopamine(user, amount);
    }

    @Override
    public void enterSerotonine(Long id, int amount) {
        PreCog preCogToService = findPreCogById(id);
        int maxPossibleAmount = 100;
        int currentAmount = preCogToService.getSerotoninLevel();
        int newAmount = currentAmount + amount;
        if(newAmount > maxPossibleAmount){
            throw new NotValidArgumentException("Too much serotonine. Maximum possible amount is: "
                    + maxPossibleAmount);
        }
        preCogToService.setSerotoninLevel(newAmount);
        preCogRepository.save(preCogToService);
        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        statisticService.enteredSerotonine(user, amount);
    }

    @Override
    public void enterDepressant(Long id, int amount) throws IllegalArgumentException {
        PreCog preCogToService = findPreCogById(id);
        int minPossibleAmount = 0;
        int currentAmount = preCogToService.getStressLevel();
        int newAmount = currentAmount - amount;
        if(newAmount < minPossibleAmount){
            throw new NotValidArgumentException("Too much depressants. Minimum possible level is: "
                    + minPossibleAmount);
        }
        preCogToService.setStressLevel(newAmount);
        preCogRepository.save(preCogToService);
        String login = securityUtil.getSessionUser();
        UserEntity user = userRepository.findByLogin(login);
        statisticService.enteredDepressant(user, amount);
    }
}
