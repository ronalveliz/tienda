package com.backend.controller;

import com.backend.exception.UnauthorizedException;
import com.backend.model.RolName;
import com.backend.model.Store;
import com.backend.model.User;
import com.backend.repository.StoreRepository;
import com.backend.security.JwtTokenUtils;
import com.backend.service.FileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;



@CrossOrigin("*")
@Slf4j
@RestController
@AllArgsConstructor
public class StoreController {
    private final StoreRepository storeRepository;
    private FileService fileService;

    @GetMapping("/my-tienda")
    public ResponseEntity<List<Store>> getMyRestaurants() {
        User currentUser = JwtTokenUtils.getCurrentUser().orElseThrow(() -> new RuntimeException("No autenticado"));
        List<Store> myRestaurants = storeRepository.findByOwnerId(currentUser.getId());
        return ResponseEntity.ok(myRestaurants);
    }

    @GetMapping("tienda/edit/{id}")
    public ResponseEntity<Boolean> canEditRestaurant(@PathVariable Long id) {
        User currentUser = JwtTokenUtils.getCurrentUser().orElseThrow(() -> new RuntimeException("No autenticado"));
        boolean canEdit = storeRepository.existsByOwner_IdAndId(currentUser.getId(), id);

        if(currentUser.getRolName() == RolName.ADMIN || canEdit)
            return ResponseEntity.ok(true);
        else
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
    }


    @GetMapping("/tienda")
    public ResponseEntity<List<Store>> findAll(@RequestParam(required = false) String name) {
        List<Store> stores;
        if (name != null && !name.isEmpty()) {
            stores = storeRepository.findByNameContainingIgnoreCase(name);
        } else {
            stores = storeRepository.findAll();
        }
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/tienda/{id}")
    public ResponseEntity<Store> findById(@PathVariable  Long id) {
        return storeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tienda")
    public Store create(@RequestParam(value = "photo", required = false) MultipartFile file,
                             @RequestParam MultiValueMap<String, String> formData) {
        Store stores = new Store();
        stores.setName(formData.getFirst("name"));
        stores.setLocation(formData.getFirst("location"));
        //stores.setPhone(formData.getFirst("phone"));


        if (file != null && !file.isEmpty()) {
            String fileName = fileService.store(file);
            stores.setImageUrl(fileName);
        }
        return this.storeRepository.save(stores);
    }



    @PutMapping("/tineda/{id}")
    public Store updateRestaurant(@PathVariable Long id,
                                       @RequestParam(value = "photo", required = false) MultipartFile file,
                                       @RequestParam MultiValueMap<String, String> formData) {

        User currentUser = JwtTokenUtils.getCurrentUser().orElseThrow(() -> new RuntimeException("No autenticado"));
        boolean canEdit = storeRepository.existsByOwner_IdAndId(currentUser.getId(), id);
        if (!(currentUser.getRolName() == RolName.ADMIN || canEdit)) {
            throw new UnauthorizedException("No puede editar");
        }

        // Busca el restaurante existente por ID en lugar de crear uno nuevo
        Store store = storeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado con id: " + id));

        // Actualiza los campos del restaurante existente
        store.setName(formData.getFirst("name"));
        store.setLocation(formData.getFirst("city"));
        store.setImageUrl(formData.getFirst("ImageUrl"));

        if (file != null && !file.isEmpty()) {
            String fileName = fileService.store(file);
            store.setImageUrl(fileName);
        }

        // Guarda y devuelve el restaurante actualizado
        return storeRepository.save(store);
    }



    @DeleteMapping("/tienda/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (storeRepository.existsById(id)) {
            storeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/restaurant/filter")
    public ResponseEntity<List<Store>> findAllFiltering(@RequestBody Store store) {
        Example<Store> filter = Example.of(store);
        List<Store> restaurants = storeRepository.findAll(filter);
        if (restaurants.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(restaurants);
    }
}
