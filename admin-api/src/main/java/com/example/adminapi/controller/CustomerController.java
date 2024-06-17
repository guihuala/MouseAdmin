package com.example.adminapi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminapi.entity.Customer;
import com.example.adminapi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/paged")
    public IPage<Customer> getAllCustomersPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Customer> customerPage = new Page<>(page, size);
        return customerService.getAllCustomersPaged(customerPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);//找到了，返回200
        } else {
            return ResponseEntity.notFound().build();//没有找到，返回404
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Customer>> getCustomerByName(@PathVariable String name) {
        List<Customer> customers = customerService.getCustomerByName(name);
        if (customers != null && !customers.isEmpty()) {
            return ResponseEntity.ok(customers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/address/{address}")
    public ResponseEntity<List<Customer>> getCustomerByAddress(@PathVariable String address) {
        List<Customer> customers = customerService.getCustomerByAddress(address);
        if (customers != null && !customers.isEmpty()) {
            return ResponseEntity.ok(customers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/post")
    public Customer createCustomer(@RequestBody Customer customer) {
        customerService.insertCustomer(customer);
        return customer;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        if (customerService.getCustomerById(id) != null) {
            customer.setId(id);
            customerService.updateCustomer(customer);
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (customerService.getCustomerById(id) != null) {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSelectedCustomers(@RequestBody List<Long> ids) {
        //customerService.deleteSelectedCustomers(ids);
        return ResponseEntity.ok("Customers deleted successfully");
    }
}

