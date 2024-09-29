package com.backend.controller;

import com.backend.model.Productos;
import com.backend.repository.ProductoRepository;
import com.backend.service.FileService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@Slf4j
public class ProductosController {


    private ProductoRepository productoRepository;
    private FileService fileService;

    @GetMapping("productos")
    public List<Productos> findAll() {
        log.info("REST request to finAll productos");
        return this.productoRepository.findAll();
    }


    @GetMapping("productos/{id}")
    private ResponseEntity<Productos> finById(@PathVariable Long id){
        Optional<Productos> productos= productoRepository.findById(id);
        if (productos.isPresent()) {
            return ResponseEntity.ok(productos.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("productos")
    public Productos create(@RequestParam(value = "photo", required = false) MultipartFile file, Productos productos) {
        if (file != null) {
            String fileName = fileService.store(file);
            productos.setPhotoUrl(fileName);
        } else {
            productos.setPhotoUrl("avatar.png");
        }
        return this.productoRepository.save(productos);
    }

    @PutMapping("productos/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            Productos productos,
            @RequestParam(value = "photo", required = false) MultipartFile file) {
        if (!this.productoRepository.existsById(id))
            return ResponseEntity.notFound().build();

        if (file != null && !file.isEmpty()) {
            String fileName = fileService.store(file);
            productos.setPhotoUrl(fileName);
        }
        return ResponseEntity.ok(this.productoRepository.save(productos));
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
