package com.makeupstore.MakeupInventory.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@Entity
@Table(name = "makeup_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MakeupProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("ProductName")
    @NotBlank(message = "El nombre del producto es requerido")
    @Size(min = 3, max = 100, message = "\n" +
            "El nombre del producto debe tener entre 3 y 100 caracteres")
    private String productName;

    @JsonProperty("ProductCategory")
    @NotBlank(message = "La categoria del producto es requerida")
    @Size(min = 3, max = 100, message = "\n" +
            "La categoría del producto debe tener entre 3 y 100 caracteres")
    private String category;

    @JsonProperty("ProductQuantity")
    @NotNull(message = "Cantidad es requerida")
    private Integer productQuantity;

    @PrePersist
    public void generateUUID() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
