package com.backend.controller;

import com.backend.model.Orderss;
import com.backend.repository.OrderssRepository;
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
public class OrderssController {
    private final OrderssRepository orderssRepository;

    @GetMapping("orders")
    private List<Orderss> findAll(){

        return orderssRepository.findAll();
    }

    @GetMapping("orders/{id}")
    public ResponseEntity<Orderss> getById(@PathVariable Long id){
        Optional<Orderss> orderss = orderssRepository.findById(id);
        if (orderss.isPresent()){
            return ResponseEntity.ok(orderss.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("orders")
    public ResponseEntity<Orderss> create(@RequestBody Orderss orderss){
       Orderss saveOrderss = orderssRepository.save(orderss);
        return ResponseEntity.ok(saveOrderss);
    }

    @PutMapping("orders/{id}")
    private ResponseEntity<Orderss> update(@RequestBody Orderss orderss, @PathVariable Long id){
        if (orderss.getId() == null){
            return ResponseEntity.badRequest().build();
        }
        Orderss saveAddress = orderssRepository.save(orderss);
        return ResponseEntity.ok(saveAddress);
    }
    @DeleteMapping("orders/{id}")
    public ResponseEntity<Orderss> deleteById(@PathVariable Long id){
        orderssRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
