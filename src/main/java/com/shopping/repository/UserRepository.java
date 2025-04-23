package com.shopping.repository;

import com.shopping.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    User findByEmail(String email);

}
