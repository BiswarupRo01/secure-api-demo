package com.biswacodes.secure_api_demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biswacodes.secure_api_demo.dao.EmployeeDAO;
import com.biswacodes.secure_api_demo.entity.Employee;
import com.biswacodes.secure_api_demo.rest.EmployeeNotFoundException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeDAO employeeDAO;

    @Override
    public List<Employee> findAll() {
        
        return employeeDAO.findAll();
    }

    @Override
    public Employee findById(int id) {
        
        Optional<Employee> dbEmployee = employeeDAO.findById(id);

        if (!dbEmployee.isPresent()) {
            throw new EmployeeNotFoundException("Employee not found. ID - " + id);
        }

        return dbEmployee.get();
    }

    @Override
    public Employee save(Employee employee) {
        
        return employeeDAO.save(employee);
    }

    @Override
    public void deleteById(int id) {
        
        employeeDAO.deleteById(id);
    }
    
}
