package com.backend.controller;

import com.backend.model.User;
import com.backend.repository.UserRepository;
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
public class UserController {
    private final UserRepository repo;

    @GetMapping("user")
    private List<User> findAll(){
        return repo.findAll();
    }


    @GetMapping("user/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id){
        Optional<User> customer = repo.findById(id);
        if (customer.isPresent()){
            return ResponseEntity.ok(customer.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("user")
    private ResponseEntity<User> create(@RequestBody User user){
        User saveUser = repo.save(user);
        return ResponseEntity.ok(saveUser);
    }

    @PutMapping ("user/{id}")
    private ResponseEntity<User> update(@RequestBody User user, @PathVariable Long id){

        if (user.getId() == null){
            return ResponseEntity.badRequest().build();
        }
        User saveList = repo.save(user);
        return ResponseEntity.ok(saveList);
    }
    @DeleteMapping("user/{id}")
    private ResponseEntity<User> delete(@PathVariable Long id) {
        repo.deleteById(id);
       return ResponseEntity.ok().build();
    }
}
