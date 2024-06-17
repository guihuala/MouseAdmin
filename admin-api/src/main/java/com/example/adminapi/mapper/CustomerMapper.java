package com.example.adminapi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminapi.entity.Customer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CustomerMapper{
    @Select("SELECT * FROM customers WHERE id = #{id}")
    Customer getCustomerById(Long id);

    @Select("SELECT * FROM customers WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<Customer> getCustomerByName(String name);

    @Select("SELECT * FROM customers WHERE address LIKE CONCAT('%', #{address}, '%')")
    List<Customer> getCustomerByAddress (String address);

    @Select("SELECT * FROM customers")
    List<Customer> getAllCustomers();

    @Select("SELECT * FROM customers")
    IPage<Customer> getAllCustomersPaged(Page<Customer> page);

    @Insert("INSERT INTO customers(name, email, address, created_at, updated_at) VALUES(#{name}, #{email}, #{address}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertCustomer(Customer customer);

    @Update("UPDATE customers SET name=#{name}, email=#{email}, address=#{address}, updated_at=NOW() WHERE id=#{id}")
    void updateCustomer(Customer customer);

    @Delete("DELETE FROM customers WHERE id=#{id}")
    void deleteCustomer(Long id);

    @Delete({
            "<script>",
            "DELETE FROM customers WHERE id IN",
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void deleteSelectedCustomers(@Param("ids") List<Long> ids);
}
