package com.backend.repository;

import com.backend.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductoRepository extends JpaRepository<Productos, Long> {
    List<Productos> findByCategory_Id(Long category_id);
    boolean existsByIdAndCategoryUserId(Long productId, Long userId);


}