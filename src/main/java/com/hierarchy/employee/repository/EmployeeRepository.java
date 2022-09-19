package com.hierarchy.employee.repository;

import com.hierarchy.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Employee findByName(String name);
}
