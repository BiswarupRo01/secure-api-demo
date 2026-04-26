package com.biswacodes.secure_api_demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biswacodes.secure_api_demo.entity.Employee;

public interface EmployeeDAO extends JpaRepository<Employee, Integer>{
    
}
