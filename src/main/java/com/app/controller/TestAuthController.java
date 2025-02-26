package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/method")
@PreAuthorize("denyAll()")
public class TestAuthController {

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public String helloGet(){
        return "Hello World - GET";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String helloPost(){
        return "Hello World - POST";
    }

    @PutMapping
    public String helloPut(){
        return "Hello World - PUT";
    }
}
