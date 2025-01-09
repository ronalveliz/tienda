package com.backend.repository;

import com.backend.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByNameContainingIgnoreCase(String name);

    List<Store> findByOwnerId(Long owner);

    boolean existsByOwner_Id(Long id);

    boolean existsByOwner_IdAndId(Long userId, Long id);


}