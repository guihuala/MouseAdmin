package com.example.adminapi.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminapi.entity.Employee;
import com.example.adminapi.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    public List<Employee> getAllEmployees() {
        return employeeMapper.getAllEmployees();
    }

    public Employee getEmployeeById(Long id) {
        return employeeMapper.getEmployeeById(id);
    }

    public List<Employee> getEmployeeByName(String name) {
        return employeeMapper.getEmployeeByName(name);
    }

    public List<Employee> getEmployeeByPosition(String position) {
        return employeeMapper.getEmployeeByPosition(position);
    }

    public List<Employee> getEmployeeBySalary(Double salary) {
        return employeeMapper.getEmployeeBySalary(salary);
    }

    public List<Employee> getEmployeeByHireDate(String hireDate) {
        return employeeMapper.getEmployeeByHireDate(hireDate);
    }

    public IPage<Employee> getAllEmployeesPaged(Page<Employee> page) {
        return employeeMapper.getAllEmployeesPaged(page);
    }

    public void insertEmployee(Employee employee) {
        employeeMapper.insertEmployee(employee);
    }

    public void updateEmployee(Employee employee) {
        employeeMapper.updateEmployee(employee);
    }

    public void deleteEmployee(Long id) {
        employeeMapper.deleteEmployee(id);
    }

    @Transactional
    public void deleteSelectedEmployees(List<Long> ids) {
        employeeMapper.deleteSelectedEmployees(ids);
    }
}
