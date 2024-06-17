package com.example.adminapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminapi.entity.Product;
import com.example.adminapi.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public List<Product> getAllProducts() {
        return productMapper.getAllProducts();
    }

    public IPage<Product> getAllProductsPaged(Page<Product> page) {
        return productMapper.getAllProductsPaged(page);
    }

    public Product getProductById(Long id) {
        return productMapper.getProductById(id);
    }

    public List<Product> getProductByName(String name) {
        return productMapper.getProductByName(name);
    }

    public List<Product> getProductByPrice(Double price) {
        return productMapper.getProductByPrice(price);
    }

    public void insertProduct(Product product) {
        productMapper.insertProduct(product);
    }

    public void updateProduct(Product product) {
        productMapper.updateProduct(product);
    }

    public void deleteProduct(Long id) {
        productMapper.deleteProduct(id);
    }

    public void deleteSelectedProducts(List<Long> ids) {
        productMapper.deleteSelectedProducts(ids);
    }
}
