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
@RestController
@RequestMapping("/api/v1")
public class HelloController {

    // Simple GET endpoint that returns plain text.
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

    // GET with query parameter: /api/v1/greet?name=Amusi
    @GetMapping("/greet")
    public String greet(@RequestParam(defaultValue = "Guest") String name) {
        return "Hello, " + name + "!";
    }

    // Spring equivalent of @Produces("text/plain")
    // Only matches when client accepts text/plain and responds as text/plain.
    @GetMapping(value = "/hello-text", produces = MediaType.TEXT_PLAIN_VALUE)
    public String helloText() {
        return "Plain text response from Spring Boot.";
    }

    // Spring equivalent of @Produces("application/json")
    // Response content type is forced to application/json.
    @GetMapping(value = "/hello-json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> helloJson() {
        return Map.of(
                "message", "JSON response from Spring Boot",
                "tip", "This endpoint uses produces=application/json"
        );
    }

    // In-memory storage for demo purposes (normally use a database/service layer).
    private final Map<Long, Book> books = new ConcurrentHashMap<>();

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    // GET by path variable: /api/v1/books/1
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = books.get(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    // POST creates a new resource. Returns HTTP 201 Created.
    @PostMapping(
            value = "/books",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Book> createBook(@RequestBody BookRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        long id = books.size() + 1L;
        Book created = new Book(id, request.title(), request.author());
        books.put(id, created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT updates an existing resource by ID.
    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody BookRequest request) {
        if (!books.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        Book updated = new Book(id, request.title(), request.author());
        books.put(id, updated);
        return ResponseEntity.ok(updated);
    }

    // DELETE removes a resource and returns 204 No Content.
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!books.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        books.remove(id);
        return ResponseEntity.noContent().build();
    }

    // Spring equivalent of @Consumes("text/plain") + @Produces("text/plain")
    @PostMapping(
            value = "/echo",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String echoPlainText(@RequestBody String message) {
        return "Server received: " + message;
    }

    // record -> concise DTO/entity style classes in Java.
    public record Book(Long id, String title, String author) {}

    public record BookRequest(String title, String author) {}
}
