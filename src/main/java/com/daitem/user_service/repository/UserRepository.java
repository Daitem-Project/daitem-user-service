package com.daitem.user_service.repository;

import com.daitem.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    User findById(long id);

    Optional<User> findByUsernameAndIsLockedAndIsSocial(String username, boolean isLocked, boolean isSocial);

    Optional<User> findByUsernameAndIsSocial(String username, boolean isSocial);

    Optional<User> findByUsernameAndIsLocked(String username, boolean isLocked);

    void deleteByUsername(String username);


}
