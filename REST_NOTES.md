# Spring Boot REST API Notes (Presentation Friendly)

## Core Annotations

- `@SpringBootApplication`: main entry point; enables auto-configuration + component scanning.
- `@RestController`: marks a class as REST API controller. Returned objects become JSON automatically.
- `@RequestMapping("/api/v1")`: sets a common base path for endpoints.

## Method Mapping Annotations

- `@GetMapping`: read data (HTTP GET).
- `@PostMapping`: create data (HTTP POST).
- `@PutMapping`: update data (HTTP PUT).
- `@DeleteMapping`: remove data (HTTP DELETE).

## Parameter/Body Binding

- `@PathVariable`: binds URL segment to method parameter. Example: `/books/{id}`.
- `@RequestParam`: binds query parameter. Example: `/greet?name=Amusi`.
- `@RequestBody`: binds JSON request body to Java object/record.

## Response Handling

- `ResponseEntity<T>` allows full control over status code + body.
- Typical statuses:
  - `200 OK` for successful reads/updates.
  - `201 Created` after creating a resource.
  - `204 No Content` after successful delete.
  - `400 Bad Request` for invalid input.
  - `404 Not Found` when resource does not exist.

## Endpoints Added In This Project

- `GET /api/v1/hello`
- `GET /api/v1/greet?name=...`
- `GET /api/v1/books`
- `GET /api/v1/books/{id}`
- `POST /api/v1/books`
- `PUT /api/v1/books/{id}`
- `DELETE /api/v1/books/{id}`

## Quick cURL Demos

```bash
curl http://localhost:8080/api/v1/hello
curl "http://localhost:8080/api/v1/greet?name=Amusi"
```

```bash
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Spring in Action\",\"author\":\"Craig Walls\"}"
```

```bash
curl http://localhost:8080/api/v1/books
curl http://localhost:8080/api/v1/books/1
```

```bash
curl -X PUT http://localhost:8080/api/v1/books/1 \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Spring Boot Upgraded\",\"author\":\"Amusi\"}"

curl -X DELETE http://localhost:8080/api/v1/books/1
```
