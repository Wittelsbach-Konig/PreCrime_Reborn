package ru.itmo.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itmo.backend.models.Role;
import ru.itmo.backend.models.UserEntity;
import ru.itmo.backend.repository.RoleRepository;
import ru.itmo.backend.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;
        List<String> roleNames = Arrays.asList("DETECTIVE", "ADMIN", "AUDITOR", "TECHNIC", "REACTIONGROUP");
        for (String roleName : roleNames) {
            createRoleIfNotFound(roleName);
        }
        Role adminRole = roleRepository.findByName("ADMIN");
        if(userRepository.findByLogin("admin") == null) {
            UserEntity user = new UserEntity();
            user.setLogin("admin");
            user.setFirstName("Vladimir");
            user.setLastName("Putin");
            user.setEmail("admin@mail.ru");
            user.setTelegramId(123456);
            user.setPassword(passwordEncoder.encode("admin"));
            Set<Role> userRole = new HashSet<>();
            userRole.add(adminRole);
            user.setRoles(userRole);
            userRepository.save(user);
        }
        alreadySetup = true;

    }

    @Transactional
    public void createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            roleRepository.save(Role.builder()
                    .name(name)
                    .build());
        }
    }
}
