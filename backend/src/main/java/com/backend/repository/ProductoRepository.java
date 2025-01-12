package com.backend.repository;

import com.backend.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductoRepository extends JpaRepository<Productos, Long> {
    List<Productos> findAllByCategory_Id(Long categoryid);

}