package com.backend.repository;

import com.backend.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByUser_Id(Long userid);
    boolean existsByUser_Id(Long userid);
    boolean existsByUser_IdAndId(Long userid, Long id);


    List<Store> findByNameContainingIgnoreCase(String name);


}