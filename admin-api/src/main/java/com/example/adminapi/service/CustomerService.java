package com.example.adminapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminapi.entity.Customer;
import com.example.adminapi.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService{
    @Autowired
    private CustomerMapper customerMapper;

    public List<Customer> getAllCustomers() {
        return customerMapper.getAllCustomers();
    }

    public IPage<Customer> getAllCustomersPaged(Page<Customer> page){
        return customerMapper.getAllCustomersPaged(page);};

    public Customer getCustomerById(Long id) {
        return customerMapper.getCustomerById(id);
    }

    public List<Customer> getCustomerByName(String name) { return customerMapper.getCustomerByName(name); }

    public List<Customer> getCustomerByAddress(String address) { return customerMapper.getCustomerByAddress(address); }

    public void insertCustomer(Customer customer) {
        customerMapper.insertCustomer(customer);
    }

    public void updateCustomer(Customer customer) {
        customerMapper.updateCustomer(customer);
    }

    public void deleteCustomer(Long id) {
        customerMapper.deleteCustomer(id);
    }

    //删除选中的客户
    @Transactional//事务
    public void deleteSelectedCustomers(List<Long> ids) {
        customerMapper.deleteSelectedCustomers(ids);
    }
}

