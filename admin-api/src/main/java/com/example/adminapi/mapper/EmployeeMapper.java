package com.example.adminapi.mapper;

import com.example.adminapi.entity.Employee;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    @Select("SELECT * FROM employees WHERE id = #{id}")
    Employee getEmployeeById(Long id);

    @Select("SELECT * FROM employees WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<Employee> getEmployeeByName(String name);

    @Select("SELECT * FROM employees WHERE position LIKE CONCAT('%', #{position}, '%')")
    List<Employee> getEmployeeByPosition(String position);

    @Select("SELECT * FROM employees WHERE salary = #{salary}")
    List<Employee> getEmployeeBySalary(Double salary);

    @Select("SELECT * FROM employees WHERE hire_date = #{hireDate}")
    List<Employee> getEmployeeByHireDate(String hireDate);

    @Select("SELECT * FROM employees")
    List<Employee> getAllEmployees();

    @Select("SELECT * FROM employees")
    IPage<Employee> getAllEmployeesPaged(Page<Employee> page);

    @Insert("INSERT INTO employees(name, email, position, salary, hire_date) VALUES(#{name}, #{email}, #{position}, #{salary}, #{hireDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertEmployee(Employee employee);

    @Update("UPDATE employees SET name=#{name}, email=#{email}, position=#{position}, salary=#{salary}, hire_date=#{hireDate} WHERE id=#{id}")
    void updateEmployee(Employee employee);

    @Delete("DELETE FROM employees WHERE id=#{id}")
    void deleteEmployee(Long id);

    @Delete({
            "<script>",
            "DELETE FROM employees WHERE id IN",
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void deleteSelectedEmployees(@Param("ids") List<Long> ids);

}
