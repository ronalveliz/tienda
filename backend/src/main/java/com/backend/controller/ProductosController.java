package com.backend.controller;

import com.backend.model.Productos;
import com.backend.model.ShoppingList;
import com.backend.repository.ProductoRepository;
import com.backend.service.FileService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        if (productos.isPresent() ) {
            return ResponseEntity.ok(productos.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("productos")
        private Productos create(@RequestParam("photo")MultipartFile file, Productos productos){
            System.out.println(file.getOriginalFilename());
            System.out.println(productos.getName());
            productos.setPhotoUrl(fileService.store(file));

        return  this.productoRepository.save(productos);
    }

    @PutMapping("productos/{id}")
    private ResponseEntity<Productos> update(@RequestBody Productos productos,@PathVariable Long id){
        if (productos.getId() == null){
            return ResponseEntity.badRequest().build();
        }
        Productos saveProductos = productoRepository.save(productos);
        return ResponseEntity.ok(saveProductos);
    }

    @DeleteMapping("productos/{id}")
    public ResponseEntity<Productos> deleteById(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
