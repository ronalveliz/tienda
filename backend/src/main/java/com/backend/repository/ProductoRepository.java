package com.backend.repository;

import com.backend.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductoRepository extends JpaRepository<Productos, Long> {
    @Query("select p from Productos p where p.category.id = ?1")
    List<Productos> findByCategory_Id(Long id);


}