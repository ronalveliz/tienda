package com.backend.repository;

import com.backend.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductoRepository extends JpaRepository<Productos, Long> {



}