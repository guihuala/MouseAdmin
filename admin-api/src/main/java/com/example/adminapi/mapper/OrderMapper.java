package com.example.adminapi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminapi.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("SELECT o.*, c.name as customerName, e.name as employeeName, p.name as productName " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.customerId = c.id " +
            "LEFT JOIN employees e ON o.employeeId = e.id " +
            "LEFT JOIN products p ON o.productId = p.id")
    List<Order> getAllOrders();

    @Select("SELECT o.*, c.name as customerName, e.name as employeeName, p.name as productName " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.customerId = c.id " +
            "LEFT JOIN employees e ON o.employeeId = e.id " +
            "LEFT JOIN products p ON o.productId = p.id")
    IPage<Order> getAllOrdersPaged(Page<Order> page);

    @Select("SELECT o.*, c.name as customerName, e.name as employeeName, p.name as productName " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.customerId = c.id " +
            "LEFT JOIN employees e ON o.employeeId = e.id " +
            "LEFT JOIN products p ON o.productId = p.id " +
            "WHERE o.id = #{id}")
    Order getOrderById(Long id);

    @Select("SELECT o.*, c.name as customerName, e.name as employeeName, p.name as productName " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.customerId = c.id " +
            "LEFT JOIN employees e ON o.employeeId = e.id " +
            "LEFT JOIN products p ON o.productId = p.id " +
            "WHERE o.customerId = #{customerId}")
    List<Order> getOrdersByCustomerId(Long customerId);

    @Select("SELECT o.*, c.name as customerName, e.name as employeeName, p.name as productName " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.customerId = c.id " +
            "LEFT JOIN employees e ON o.employeeId = e.id " +
            "LEFT JOIN products p ON o.productId = p.id " +
            "WHERE o.employeeId = #{employeeId}")
    List<Order> getOrdersByEmployeeId(Long employeeId);

    @Select("SELECT o.*, c.name as customerName, e.name as employeeName, p.name as productName " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.customerId = c.id " +
            "LEFT JOIN employees e ON o.employeeId = e.id " +
            "LEFT JOIN products p ON o.productId = p.id " +
            "WHERE o.productId = #{productId}")
    List<Order> getOrdersByProductId(Long productId);

    @Select("SELECT o.*, c.name as customerName, e.name as employeeName, p.name as productName " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.customerId = c.id " +
            "LEFT JOIN employees e ON o.employeeId = e.id " +
            "LEFT JOIN products p ON o.productId = p.id " +
            "WHERE o.orderNumber = #{orderNumber}")
    List<Order> getOrdersByOrderNumber(String orderNumber);

    @Insert("INSERT INTO orders(customerId, employeeId, productId, orderNumber, qty, totalAmount, orderDate) VALUES(#{customerId}, #{employeeId}, #{productId}, #{orderNumber}, #{qty}, #{totalAmount}, #{orderDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertOrder(Order order);

    @Update("UPDATE orders SET customerId=#{customerId}, employeeId = #{employeeId},productId = #{productId},orderNumber=#{orderNumber}, totalAmount=#{totalAmount}, orderDate=#{orderDate} WHERE id=#{id}")
    void updateOrder(Order order);

    @Delete("DELETE FROM orders WHERE id=#{id}")
    void deleteOrder(Long id);

    @Delete({
            "<script>",
            "DELETE FROM orders WHERE id IN",
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void deleteSelectedOrders(@Param("ids") List<Long> ids);
}