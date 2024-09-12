package com.backend.controller;

import com.backend.model.Store;
import com.backend.repository.StoreRepository;
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
public class StoreController {
    private final StoreRepository repo;

    // Obtener todas las tiendas
    @GetMapping("store")
    private List<Store> findAll(){
        return repo.findAll();
    }

    // Obtener una tienda por ID
    @GetMapping("store/{id}")
    public ResponseEntity<Store> getById(@PathVariable Long id) {
        Optional<Store> store = repo.findById(id);
        if (store.isPresent()) {
            return ResponseEntity.ok(store.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear una nueva tienda
    @PostMapping("store")
    public ResponseEntity<Store> create(@RequestBody Store store) {
        Store savedStore = repo.save(store);
        return ResponseEntity.ok(savedStore);
    }

    // Actualizar una tienda existente
    @PutMapping("store/{id}")
    public ResponseEntity<Store> update(@RequestBody Store store, @PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        store.setId(id); // Asegurar que el ID de la tienda se actualiza correctamente
        Store updatedStore = repo.save(store);
        return ResponseEntity.ok(updatedStore);
    }

    // Eliminar una tienda por ID
    @DeleteMapping("store/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
