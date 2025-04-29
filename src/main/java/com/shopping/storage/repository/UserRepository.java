package com.shopping.storage.repository;

import com.shopping.storage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(Long id);
    Optional<User> findByNickname(String nickname);

}
