package com.sneakysquid.authenticator.repository;

import com.sneakysquid.authenticator.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findUserById(Long id);
    Optional<User> findUserByUsername(String username);
    List<User> findByUsernameContainingIgnoreCaseOrderByUsernameAsc(String username);
}
