package com.sneakysquid.authenticator.repository;

import com.sneakysquid.authenticator.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

}
