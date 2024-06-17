package com.example.adminapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.adminapi.entity.Employee;
import com.example.adminapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Employee>> getEmployeeByName(@PathVariable String name) {
        List<Employee> employees = employeeService.getEmployeeByName(name);
        if (employees != null && !employees.isEmpty()) {
            return ResponseEntity.ok(employees);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<Employee>> getEmployeeByPosition(@PathVariable String position) {
        List<Employee> employees = employeeService.getEmployeeByPosition(position);
        if (employees != null && !employees.isEmpty()) {
            return ResponseEntity.ok(employees);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/salary/{salary}")
    public ResponseEntity<List<Employee>> getEmployeeBySalary(@PathVariable Double salary) {
        List<Employee> employees = employeeService.getEmployeeBySalary(salary);
        if (employees != null && !employees.isEmpty()) {
            return ResponseEntity.ok(employees);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/hireDate/{hireDate}")
    public ResponseEntity<List<Employee>> getEmployeeByHireDate(@PathVariable String hireDate) {
        List<Employee> employees = employeeService.getEmployeeByHireDate(hireDate);
        if (employees != null && !employees.isEmpty()) {
            return ResponseEntity.ok(employees);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/paged")
    public IPage<Employee> getAllEmployeesPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Employee> employeePage = new Page<>(page, size);
        return employeeService.getAllEmployeesPaged(employeePage);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        employeeService.insertEmployee(employee);
        return employee;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        if (employeeService.getEmployeeById(id) != null) {
            employee.setId(id);
            employeeService.updateEmployee(employee);
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (employeeService.getEmployeeById(id) != null) {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteSelected")
    public ResponseEntity<Void> deleteSelectedEmployees(@RequestBody List<Long> ids) {
        employeeService.deleteSelectedEmployees(ids);
        return ResponseEntity.noContent().build();
    }
}

