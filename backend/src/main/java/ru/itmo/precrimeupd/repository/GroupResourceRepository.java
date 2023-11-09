package ru.itmo.precrimeupd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.GroupResource;
import ru.itmo.precrimeupd.model.GroupResourceType;

import java.util.List;

public interface GroupResourceRepository extends JpaRepository<GroupResource, Long> {
    List<GroupResource> findByType(GroupResourceType type);
    GroupResource findByResourceName(String resourceName);
}
