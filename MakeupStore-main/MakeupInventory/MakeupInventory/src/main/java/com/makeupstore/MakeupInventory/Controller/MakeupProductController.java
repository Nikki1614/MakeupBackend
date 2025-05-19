package com.makeupstore.MakeupInventory.Controller;

import com.makeupstore.MakeupInventory.Entities.MakeupProductEntity;
import com.makeupstore.MakeupInventory.Service.MakeupProductService;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID; 

@RestController
@RequestMapping("/api/v1/makeupproducts")
@Validated 

public class MakeupProductController {
    private final MakeupProductService makeupProductService;

    public MakeupProductController(MakeupProductService makeupProductService) {
        this.makeupProductService = makeupProductService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "productName,asc") String[] sort) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
            return makeupProductService.getAllProducts(pageable);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid sorting direction. Use 'asc' or 'desc'.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        return makeupProductService.getProductById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getProductsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "productName,asc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
        return makeupProductService.getProductsByName(name, pageable);
    }

    @PostMapping
    public ResponseEntity<?> insertProduct(@Valid @RequestBody MakeupProductEntity makeupProductEntity) {
        return makeupProductService.addProduct(makeupProductEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @Valid @RequestBody MakeupProductEntity makeupProductEntity) {
        return makeupProductService.updateProduct(id, makeupProductEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        return makeupProductService.deleteProduct(id);
    }

    private Sort.Order parseSort(String[] sort) {
        if (sort.length < 2) {
            throw new IllegalArgumentException("El parametro Sort debe tener campo y direccion (e.g., 'productName,desc').");
        }

        String property = sort[0];
        String direction = sort[1].toLowerCase();

        List<String> validDirections = Arrays.asList("asc", "desc");
        if (!validDirections.contains(direction)) {
            throw new IllegalArgumentException("Invalid sort direction. Use 'asc' or 'desc'.");
        }

        return new Sort.Order(Sort.Direction.fromString(direction), property);
    }
}
