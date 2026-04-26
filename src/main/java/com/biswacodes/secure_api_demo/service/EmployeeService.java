package com.biswacodes.secure_api_demo.service;

import java.util.List;

import com.biswacodes.secure_api_demo.entity.Employee;

public interface EmployeeService {
    
    List<Employee> findAll();

    Employee findById(int id);

    Employee save(Employee employee);

    void deleteById(int id);
}
