package com.makeupstore.MakeupInventory.Entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
    
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
        @NotBlank(message = "Product name is required")
        @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
        private String productName;
    
        @JsonProperty("ProductCategory")
        @NotBlank(message = "Product category is required")
        @Size(min = 3, max = 100, message = "Product category must be between 3 and 100 characters")
        private String category;
    
        @JsonProperty("MakeupProductQuantity")
        @NotNull(message = "Quantity is required")
        private Integer productQuantity;

        @PrePersist
        public void generateUUID() {
            if (id == null) {
                id = UUID.randomUUID();
            }
        }

        @Override
        public String toString() {
            return "MakeupProductEntity{" +
                    "id=" + id +
                    ", productName'" + productName + '\'' +
                    ", productCategory='" + category + '\'' +
                    ", productQuantity=" + productQuantity +
                    '}';
        }

        public UUID getId(){
            return id;
        }

        public String getMakeupProductName(){
            return productName;
        }

        public void setMakeupProductName(String productName){
            this.productName = productName;
        }

        public String getMakeupProductCategory() {
            return category;
        }
    
        public void setMakeupProductCategory(String category) {
            this.category = category;
        }

        public Integer getProductQuantity() {
            return productQuantity;
        }

        public void setProductQuantity(Integer productQuantity) {
           this.productQuantity = productQuantity;
        }
    
    }
