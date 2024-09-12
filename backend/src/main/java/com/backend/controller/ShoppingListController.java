package com.backend.controller;

import com.backend.model.ShoppingList;
import com.backend.repository.ShoppingListRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
public class ShoppingListController {

    private final ShoppingListRepository shoppingListRepository;

    @GetMapping("carrito")
    private List<ShoppingList> findAll(){

        return shoppingListRepository.findAll();
    }

    @GetMapping("carrito/{id}")
    public ResponseEntity<ShoppingList> getById(@PathVariable Long id){
       Optional<ShoppingList> shoppingList = shoppingListRepository.findById(id);
        if (shoppingList.isPresent()){
            return ResponseEntity.ok(shoppingList.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

   @PostMapping("carrito")
   private ResponseEntity<ShoppingList> create(@RequestBody ShoppingList shoppingList){
       ShoppingList saveListShop = shoppingListRepository.save(shoppingList);
       return ResponseEntity.ok(saveListShop);
   }

   @PutMapping("carrito/{id}")
   private ResponseEntity<ShoppingList> update(@RequestBody ShoppingList shoppingList, @PathVariable Long id){
       if (shoppingList.getId() == null){
           return ResponseEntity.badRequest().build();
       }
       ShoppingList saveList = shoppingListRepository.save(shoppingList);
       return ResponseEntity.ok(saveList);
   }
   @DeleteMapping("carrito/{id}")
    public ResponseEntity<ShoppingList> deleteById(@PathVariable Long id){
    shoppingListRepository.deleteById(id);
       return ResponseEntity.ok().build();
   }
}
