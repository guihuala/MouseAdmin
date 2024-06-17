package com.example.adminapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminapi.entity.Order;
import com.example.adminapi.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    public IPage<Order> getAllOrdersPaged(Page<Order> page) {
        return orderMapper.getAllOrdersPaged(page);
    }

    public Order getOrderById(Long id) {
        return orderMapper.getOrderById(id);
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderMapper.getOrdersByCustomerId(customerId);
    }

    public List<Order> getOrdersByEmployeeId(Long employeeId) {
        return orderMapper.getOrdersByEmployeeId(employeeId);
    }

    public List<Order> getOrdersByProductId(Long productId) {
        return orderMapper.getOrdersByProductId(productId);
    }

    public List<Order> getOrdersByOrderNumber(String orderNumber) {
        return orderMapper.getOrdersByOrderNumber(orderNumber);
    }

    public void insertOrder(Order order) {
        orderMapper.insertOrder(order);
    }

    public void updateOrder(Order order) {
        orderMapper.updateOrder(order);
    }

    public void deleteOrder(Long id) {
        orderMapper.deleteOrder(id);
    }

    public void deleteSelectedOrders(List<Long> ids) {
        orderMapper.deleteSelectedOrders(ids);
    }
}
