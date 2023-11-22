package com.fos.api.repository;

import com.fos.api.model.User;
import com.fos.api.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserSub(String userSub);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByUserNameAndIdNot(String userName, Integer id);
}
