package com.biswacodes.secure_api_demo.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biswacodes.secure_api_demo.entity.Employee;
import com.biswacodes.secure_api_demo.service.EmployeeService;

import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    JsonMapper jsonMapper;


    // crud operations via REST service

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {

        employee.setId(0);

        Employee dbEmployee = employeeService.save(employee);

        URI location = null;

        try {
            location = new URI("api/employees/" + dbEmployee.getId());
        } catch (URISyntaxException e) {
            // ignore
        }

        return ResponseEntity.created(location).body(dbEmployee);
    }

    
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        
        return ResponseEntity.ok().body(employeeService.findAll());
    }


    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employeeId") int id) {

        return ResponseEntity.ok().body(employeeService.findById(id));
    }


    @PutMapping("/employees") 
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {

        Employee updatedEmployee = employeeService.save(employee);
        
        return ResponseEntity.ok().body(updatedEmployee);
    }


    @PatchMapping("employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(name="employeeId") int id, 
                                                    @RequestBody Map<String,Object> patchPayload) {
        
        if (patchPayload.containsKey("id")) {
            throw new RuntimeException("The request body must not contain any ID. ID - " + patchPayload.get("id"));
        }

        Employee tempEmployee = employeeService.findById(id);

        Employee patchedEmployee = jsonMapper.updateValue(tempEmployee, patchPayload);
        
        return ResponseEntity.ok().body(employeeService.save(patchedEmployee));
    }


    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") int id) {

        Employee tempEmployee = employeeService.findById(id);

        employeeService.deleteById(tempEmployee.getId());
        
        return ResponseEntity.ok().body("Employee deleted. ID - " + tempEmployee.getId());
    }


    // handle exceptions 

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeNotFoundException exc) {

        EmployeeErrorResponse error = new EmployeeErrorResponse();

        error.setResponseCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(Instant.now().toString());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(Exception exc) {

        EmployeeErrorResponse error = new EmployeeErrorResponse();

        error.setResponseCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(Instant.now().toString());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
}
