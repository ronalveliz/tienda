package com.backend.controller;
import com.backend.model.Productos;
import com.backend.repository.ProductoRepository;
import com.backend.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    private final ProductoRepository productoRepository;
    private FileService fileService;



    @GetMapping("productos")
    public ResponseEntity<List<Productos>> findAll() {
        List<Productos> dishes = productoRepository.findAll();
        if (dishes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("productos/{id}")
    public ResponseEntity<Productos> findById(@PathVariable Long id) {
        Optional<Productos> optionalDish = productoRepository.findById(id);
        if (optionalDish.isPresent()) {
            return ResponseEntity.ok(optionalDish.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("Productos/filter-by-category/{id}")
    public List<Productos> findAllByMenuId(@PathVariable Long id){
        return this.productoRepository.findAllByCategory_Id(id);
    }


    @PostMapping("Productos")
    public Productos create(
            @RequestParam(value = "photo", required = false) MultipartFile file,
            Productos productos){

        if(file != null && !file.isEmpty()) {
            String fileName = fileService.store(file);
            productos.setPhotoUrl(fileName);
        } else {
            productos.setPhotoUrl("avatar.png");
        }

        return this.productoRepository.save(productos);
    }


    @PutMapping("dishes/{id}")
    public ResponseEntity<Productos> update(
            @PathVariable Long id,
            Productos productos,
            @RequestParam(value = "photo", required = false) MultipartFile file
    ){
        if(!this.productoRepository.existsById(id))
            return ResponseEntity.notFound().build();

        if(file != null && !file.isEmpty()) {
            String fileName = fileService.store(file);
            productos.setPhotoUrl(fileName);
        }
        return ResponseEntity.ok(this.productoRepository.save(productos));
    }

    @DeleteMapping("productos/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Ocurrio un Error al eliminar su reserva nro: {}:{}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
