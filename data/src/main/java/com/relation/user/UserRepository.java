package com.relation.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            select u
            from User u
            join fetch u.account a
            where a.email = :email
           """)
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "account")
    Optional<User> findById(Long id);

    Optional<User> findByNickname(String nickname);

}
