package com.backend.controller;

import com.backend.model.Productos;
import com.backend.repository.ProductoRepository;
import com.backend.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@Slf4j
public class ProductosController {

    private final ProductoRepository productoRepository;
    private FileService fileService;

    // Endpoints públicos (acceso sin autenticación)
    @GetMapping("productos")
    public ResponseEntity<List<Productos>> findAll() {
        List<Productos> productos = productoRepository.findAll();
        if (productos.isEmpty()) {
            log.info("No se encontraron productos.");
            return ResponseEntity.notFound().build();
        }
        log.info("Productos encontrados: {}", productos.size());
        return ResponseEntity.ok(productos);
    }

    @GetMapping("productos/{id}")
    public ResponseEntity<Productos> findById(@PathVariable Long id) {
        Optional<Productos> optionalDish = productoRepository.findById(id);
        return optionalDish.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("productos/filter-by-category/{id}")
    public List<Productos> findAllByMenuId(@PathVariable Long id) {
        return this.productoRepository.findAllByCategory_Id(id);
    }

    @GetMapping("productos/search")
    public ResponseEntity<List<Productos>> searchByName(@RequestParam String nombre) {
        List<Productos> productos = productoRepository.findByNombreContainingIgnoreCase(nombre);
        return productos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(productos);
    }

    @GetMapping("productos/price-range")
    public ResponseEntity<List<Productos>> findByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {
        List<Productos> productos = productoRepository.findByPrecioBetween(minPrice, maxPrice);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("productos/published")
    public ResponseEntity<List<Productos>> findByPublished(@RequestParam boolean published) {
        List<Productos> productos = productoRepository.findByPublished(published);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("productos/disponible")
    public ResponseEntity<List<Productos>> findDisponibleProducts() {
        List<Productos> productos = productoRepository.findByDisponibleTrue();
        return ResponseEntity.ok(productos);
    }

    // Endpoints protegidos (requieren autenticación y roles)
    @PostMapping("productos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TIENDA')")
    public ResponseEntity<Productos> create(
            @RequestParam(value = "photo", required = false) MultipartFile file,
            @RequestBody Productos productos) {

        try {
            if(file != null && !file.isEmpty()) {
                String fileName = fileService.store(file);
                productos.setPhotoUrl(fileName);
            } else {
                productos.setPhotoUrl("avatar.png");
            }

            Productos savedProduct = this.productoRepository.save(productos);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            log.error("Error al crear producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("productos/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TIENDA')")
    public ResponseEntity<Productos> update(
            @PathVariable Long id,
            @RequestBody Productos productos,
            @RequestParam(value = "photo", required = false) MultipartFile file) {

        if(!this.productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            if(file != null && !file.isEmpty()) {
                String fileName = fileService.store(file);
                productos.setPhotoUrl(fileName);
            }
            productos.setId(id); // Asegurar que el ID sea el correcto
            Productos updatedProduct = this.productoRepository.save(productos);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            log.error("Error al actualizar producto ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            if (!productoRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar producto ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}