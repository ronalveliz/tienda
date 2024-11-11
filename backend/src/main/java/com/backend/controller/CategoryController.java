package com.backend.controller;

import com.backend.model.Category;
import com.backend.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@Slf4j
@RestController
@AllArgsConstructor
public class CategoryController {
    private final CategoryRepository repo;

    @GetMapping("category")
    private List<Category> findAll(){
        return repo.findAll();
    }


    @GetMapping("category/{id}")
    private ResponseEntity<Category> findById(@PathVariable Long id){
        Optional<Category> category = repo.findById(id);
        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category.get());
    }

    @PostMapping("category")
    private ResponseEntity<Category> create(@RequestBody Category category){
        Category savecat = repo.save(category);
        return ResponseEntity.ok(savecat);
    }

    @PutMapping("category/{id}")
    private ResponseEntity<Category> update(@RequestBody Category category, @PathVariable Long id) {
        // Verifica si el objeto Category tiene un id, y si coincide con el ID de la URL
        if (category.getId() == null || !category.getId().equals(id)) {
        }

        // Verifica si el ID existe en la base de datos
        Optional<Category> existingCategory = repo.findById(id);
        if (existingCategory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Realiza la actualización
        Category updatedCategory = repo.save(category);
        return ResponseEntity.ok(updatedCategory);
    }
    @DeleteMapping("category/{id}")
    private ResponseEntity<Category> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
