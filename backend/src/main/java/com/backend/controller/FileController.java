package com.backend.controller;

import com.backend.service.FileService;
import org.springframework.core.io.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@Slf4j
@RestController
@AllArgsConstructor
@RestControllerAdvice

public class FileController {

    private final FileService fileService;

    @GetMapping("files/{name:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String name){
        Resource file = fileService.load(name);
        return ResponseEntity.ok(file);
    }


}
