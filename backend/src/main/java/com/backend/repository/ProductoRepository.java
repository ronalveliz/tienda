package com.backend.repository;

import com.backend.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductoRepository extends JpaRepository<Productos, Long> {
    List<Productos> findAllByCategory_Id(Long categoryid);
    List<Productos> findByPublishedTrue();
    List<Productos> findByNombreContainingIgnoreCase(String nombre); // Coincide con la entidad
    List<Productos> findByPrecioBetween(double minPrice, double maxPrice); // Coincide con la entidad
    List<Productos> findByPublished(boolean published);
    List<Productos> findByDisponibleTrue();
}