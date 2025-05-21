package com.makeupstore.MakeupInventory.Service;

import com.makeupstore.MakeupInventory.Entities.MakeupProductEntity;
import com.makeupstore.MakeupInventory.Repository.MakeupProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MakeupProductService {

    private final MakeupProductRepository makeupProductRepository;

    public MakeupProductService(MakeupProductRepository makeupProductRepository) {
        this.makeupProductRepository = makeupProductRepository;
    }

    public ResponseEntity<?> getAllProducts(Pageable pageable) {
        Page<MakeupProductEntity> products = makeupProductRepository.findAll(pageable);
        return buildPagedResponse(products);
    }

    public ResponseEntity<?> getProductById(UUID id) {
        Optional<MakeupProductEntity> product = makeupProductRepository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Status", "Product with ID " + id + " not found."));
        }
        return ResponseEntity.ok(Collections.singletonMap("Product", product.get()));
    }

    public ResponseEntity<?> getProductsByName(String name, Pageable pageable) {
        Page<MakeupProductEntity> products = makeupProductRepository.findAllByProductNameContaining(name, pageable);
        return buildPagedResponse(products);
    }

    public ResponseEntity<?> addProduct(MakeupProductEntity productToAdd) {
        Page<MakeupProductEntity> existing = makeupProductRepository.findAllByProductNameContaining(
                productToAdd.getMakeupProductName(),
                Pageable.unpaged());

        if (existing.getTotalElements() > 0) {
            return new ResponseEntity<>(Collections.singletonMap("Status",
                    String.format("Product already exists with %d coincidences.", existing.getTotalElements())),
                    HttpStatus.CONFLICT);
        }

        MakeupProductEntity saved = makeupProductRepository.save(productToAdd);
        return new ResponseEntity<>(Collections.singletonMap("Status",
                String.format("Added Product with ID %s", saved.getId())), HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateProduct(UUID id, MakeupProductEntity updatedProduct) {
        Optional<MakeupProductEntity> optional = makeupProductRepository.findById(id);
        if (optional.isEmpty()) {
            return new ResponseEntity<>(Collections.singletonMap("Status",
                    String.format("Product with ID %s not found.", id)), HttpStatus.NOT_FOUND);
        }

        MakeupProductEntity existing = optional.get();
        existing.setMakeupProductName(updatedProduct.getMakeupProductName());
        existing.setMakeupProductCategory(updatedProduct.getMakeupProductCategory());
        existing.setProductQuantity(updatedProduct.getProductQuantity());

        makeupProductRepository.save(existing);

        return ResponseEntity.ok(Collections.singletonMap("Status",
                String.format("Updated Product with ID %s", existing.getId())));
    }

    public ResponseEntity<?> deleteProduct(UUID id) {
        Optional<MakeupProductEntity> product = makeupProductRepository.findById(id);
        if (product.isEmpty()) {
            return new ResponseEntity<>(Collections.singletonMap("Status",
                    String.format("Product with ID %s doesn't exist.", id)), HttpStatus.NOT_FOUND);
        }

        makeupProductRepository.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("Status",
                String.format("Deleted Product with ID %s", id)));
    }

    private ResponseEntity<?> buildPagedResponse(Page<MakeupProductEntity> products) {
        Map<String, Object> response = new HashMap<>();
        response.put("TotalElements", products.getTotalElements());
        response.put("TotalPages", products.getTotalPages());
        response.put("CurrentPage", products.getNumber());
        response.put("NumberOfElements", products.getNumberOfElements());
        response.put("Products", products.getContent());
        return ResponseEntity.ok(response);
    }
}
