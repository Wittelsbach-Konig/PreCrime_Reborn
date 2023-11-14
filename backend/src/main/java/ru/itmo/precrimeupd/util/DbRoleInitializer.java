package ru.itmo.precrimeupd.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.itmo.precrimeupd.model.Role;
import ru.itmo.precrimeupd.repository.RoleRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class DbRoleInitializer {
    private final RoleRepository roleRepository;

    @Autowired
    DbRoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void afterStartUp() {
        List<String> roleNames = Arrays.asList("DETECTIVE", "ADMIN", "AUDITOR", "TECHNIC", "REACTIONGROUP");

        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName);
            if (role == null) {
                roleRepository.save(Role.builder()
                        .name(roleName)
                        .build());
            }
        }
    }
}
