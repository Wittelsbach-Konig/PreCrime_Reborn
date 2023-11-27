package ru.itmo.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.backend.models.GroupResource;
import ru.itmo.backend.models.GroupResourceType;

import java.util.List;

public interface GroupResourceRepository extends JpaRepository<GroupResource, Long> {
    List<GroupResource> findAllByType(GroupResourceType type);
    GroupResource findByResourceName(String resourceName);
}
