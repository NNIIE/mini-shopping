package com.relation.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query("""
            select u
            from Admin u
            join fetch u.account a
            where a.email = :email
           """)
    Optional<Admin> findByEmail(String email);

}
