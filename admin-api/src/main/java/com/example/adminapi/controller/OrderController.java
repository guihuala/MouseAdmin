package com.example.adminapi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminapi.entity.Order;
import com.example.adminapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/paged")
    public IPage<Order> getAllOrdersPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Order> orderpage = new Page<>(page, size);
        return orderService.getAllOrdersPaged(orderpage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/orderNumber/{orderNumber}")
    public ResponseEntity<List<Order>> getOrdersByOrderNumber(@PathVariable String orderNumber) {
        List<Order> orders = orderService.getOrdersByOrderNumber(orderNumber);
        if (orders != null && !orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customerId/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/productId/{productId}")
    public ResponseEntity<List<Order>> getOrdersByProductId(@PathVariable Long productId) {
        List<Order> orders = orderService.getOrdersByProductId(productId);
        if (orders != null && !orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/employeeId/{employeeId}")
    public ResponseEntity<List<Order>> getOrdersByEmployeeId(@PathVariable Long employeeId) {
        List<Order> orders = orderService.getOrdersByEmployeeId(employeeId);
        return ResponseEntity.ok(orders);
    }


    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        orderService.insertOrder(order);
        return order;
    }

    @PutMapping("/{id}")
    public Order updateOrder(@RequestBody Order order) {
        orderService.updateOrder(order);
        return order;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.getOrderById(id) != null) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteSelected")
    public ResponseEntity<Void> deleteSelectedOrders(@RequestBody List<Long> ids) {
        orderService.deleteSelectedOrders(ids);
        return ResponseEntity.noContent().build();
    }
}
