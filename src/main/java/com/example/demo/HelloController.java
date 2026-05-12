package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * PRESENTATION NOTES:
 * - @RestController: marks this class as a REST endpoint provider.
 *   Spring automatically serializes return values to JSON.
 * - @RequestMapping("/api/v1"): common base URL for all endpoints in this class.
 * - Each HTTP method annotation (@GetMapping/@PostMapping/@PutMapping/@DeleteMapping)
 *   maps one Java method to one REST operation.
 * - In Spring MVC, media types are controlled with consumes/produces attributes.
 *   They are the Spring equivalent of JAX-RS @Consumes and @Produces.
 */


// Check out swagger api http://localhost:8080/swagger-ui/index.html#/
@RestController
@RequestMapping("/api/v1")
public class HelloController {

        // In-memory storage for demo purposes (normally use a database/service layer).
    private Map<Integer, String> database = new HashMap<>();

    // Simple GET endpoint that returns plain text.
    @GetMapping("/hello")
    public String hello() {
        System.out.println("Hello endpoint was called!");
        return "Hello from Spring Boot!";
    }

    // GET with query parameter: /api/v1/greet?name=Amusi
    @GetMapping("/greet")
    public String greet(@RequestParam(defaultValue = "Guest") String name) {
        System.out.println("Greet endpoint was called! with param name=" + name);
        return "Hello, " + name + "!";
    }

    @GetMapping("/{id}")
    public String getItem(@PathVariable int id) {
        System.out.println("Get item endpoint was called! with param id=" + id);
        return database.get(id);
    }

    @PostMapping("/{id}")
    public String createItem(@PathVariable int id, @RequestBody String name) {
        database.put(id, name);
        System.out.println("Created item " + id + ": " + name);
        return "Created item " + id + ": " + name;
    }

    // 4. PUT: Update an entry (e.g., /api/v1/1)
    @PutMapping("/{id}")
    public String updateItem(@PathVariable int id, @RequestBody String newName) {
        if (database.containsKey(id)) {
            database.put(id, newName);
            System.out.println("Updated item " + id + " to: " + newName);
            return "Updated item " + id + " to: " + newName;
        }
        return "Error: ID " + id + " does not exist!";
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable int id) {
        if (database.containsKey(id)) {
            database.remove(id);
            System.out.println("Deleted item " + id);
            return "Deleted item " + id;
        }
        return "Error: ID " + id + " not found!";
    }

}
