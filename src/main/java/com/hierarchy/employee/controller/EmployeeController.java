package com.hierarchy.employee.controller;

import com.hierarchy.employee.model.Employee;
import com.hierarchy.employee.service.EmployeeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(value = "Employee Api", tags = "Employee Api", produces = "application/json")
@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Map<String, Map>> postEmployees
            (@RequestBody Map<String, String> pairEmploySuper) {
        Map<String, Map> result = employeeService.postHierarchy(pairEmploySuper);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getEmployees() {
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @GetMapping("/{name}")
    public ResponseEntity<String[]> getEmployeeByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(employeeService.getSupervisor(name));
    }
}
