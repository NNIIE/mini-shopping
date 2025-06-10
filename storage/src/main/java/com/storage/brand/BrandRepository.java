package com.storage.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Page<Brand> findByAdminId(Long adminId, Pageable pageable);

    Optional<Brand> findByName(String name);

    Optional<Brand> findByIdAndAdminId(Long id, Long adminId);

}

