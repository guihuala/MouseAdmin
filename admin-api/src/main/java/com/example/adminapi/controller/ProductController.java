package com.example.adminapi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminapi.entity.Employee;
import com.example.adminapi.entity.Product;
import com.example.adminapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/paged")
    public IPage<Product> getAllProductsPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> productPage = new Page<>(page, size);
        return productService.getAllProductsPaged(productPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Product>> getProductByName(@PathVariable String name) {
        List<Product> products = productService.getProductByName(name);
        if (products != null && !products.isEmpty()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/price/{price}")
    public ResponseEntity<List<Product>> getProductByPrice(@PathVariable Double price) {
        List<Product> products = productService.getProductByPrice(price);
        if (products != null && !products.isEmpty()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        productService.insertProduct(product);
        return product;
    }

    @PutMapping("/{id}")
    public Product updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return product;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSelectedProducts(@RequestBody List<Long> ids) {
        productService.deleteSelectedProducts(ids);
        return ResponseEntity.ok().build();
    }
}
