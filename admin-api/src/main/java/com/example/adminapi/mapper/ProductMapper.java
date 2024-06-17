package com.example.adminapi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminapi.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("SELECT * FROM products")
    List<Product> getAllProducts();

    @Select("SELECT * FROM products")
    IPage<Product> getAllProductsPaged(Page<Product> page);

    @Select("SELECT * FROM products WHERE id = #{id}")
    Product getProductById(Long id);

    @Select("SELECT * FROM products WHERE name LIKE CONCAT('%',#{name},'%')")
    List<Product> getProductByName(String name);

    @Select("SELECT * FROM products WHERE price = #{price}")
    List<Product> getProductByPrice(Double price);

    @Insert("INSERT INTO products(name, description, price) VALUES(#{name}, #{description}, #{price})")
    void insertProduct(Product product);

    @Update("UPDATE products SET name=#{name}, description=#{description}, price=#{price} WHERE id=#{id}")
    void updateProduct(Product product);

    @Delete("DELETE FROM products WHERE id=#{id}")
    void deleteProduct(Long id);

    @Delete({
            "<script>",
            "DELETE FROM products WHERE id IN",
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void deleteSelectedProducts(List<Long> ids);

}
