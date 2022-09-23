package com.hierarchy.employee.service;

import com.hierarchy.employee.model.Employee;
import com.hierarchy.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class EmployeeService {

    public static final String INVALID_HIERARCHY = "Invalid Hierarchy";
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeesByName(String name) {
        return employeeRepository.findByName(name);
    }

    public String[] getSupervisor(String name) {
        Employee employee = getEmployeesByName(name);
        if (employee == null) {
            return new String[2];
        }
        String supervisor2ndName = null;
        Employee supervisor = getEmployeesByName(employee.getSupervisor());
        if (supervisor != null) {
            supervisor2ndName = supervisor.getSupervisor();
        }
        return new String[]{employee.getSupervisor(), supervisor2ndName};
    }

    public Map<String, Map> postHierarchy(Map<String, String> prerequisites) {
        Map<String, Set<String>> supervisorOf = new HashMap<>();
        Map<String, List<String>> employeesOf = new HashMap<>();
        Set<String> numEmpl = new HashSet<>();
        if (prerequisites.isEmpty()) {
            throw new RuntimeException(INVALID_HIERARCHY);
        }
        for (Map.Entry<String, String> p : prerequisites.entrySet()) {
            if (p.getKey().equals(p.getValue())) {
                throw new RuntimeException(INVALID_HIERARCHY);
            }
            supervisorOf.putIfAbsent(p.getKey(), new HashSet<>());
            supervisorOf.putIfAbsent(p.getValue(), new HashSet<>());
            employeesOf.putIfAbsent(p.getValue(), new ArrayList<>());
            employeesOf.putIfAbsent(p.getKey(), new ArrayList<>());
            numEmpl.add(p.getKey());
            numEmpl.add(p.getValue());

            supervisorOf.get(p.getKey()).add(p.getValue());
            employeesOf.get(p.getValue()).add(p.getKey());
            if (supervisorOf.get(p.getKey()).size() == 2) {
                throw new RuntimeException(p.getKey() + "have more than 1 supervisor");
            }
        }
        LinkedList<String> todo = new LinkedList<>();
        Set<Employee> setSaveEmpl = new HashSet<>();
        Map<String, Employee> mapEmpl = new HashMap<>();
        for (String c : numEmpl) {
            if (supervisorOf.get(c).isEmpty()) {
                todo.add(c);
            }
            Employee employee = getEmployeesByName(c);
            if (employee == null) {
                employee = Employee.builder().name(c).build();
            }
            mapEmpl.put(c, employee);
            setSaveEmpl.add(employee);
        }

        if (todo.size() > 1) {
            throw new RuntimeException("There had more than 1 root");
        }
        if (todo.isEmpty()) {
            throw new RuntimeException("There had a loop");
        }
        Map<String, Map> result = new HashMap<>();
        result.put(todo.getFirst(), new HashMap<>());
        dfs(result, employeesOf, mapEmpl);
        employeeRepository.deleteAll();
        employeeRepository.saveAll(setSaveEmpl);
        return result;
    }

    private void dfs(Map<String, Map> root, Map<String, List<String>> emplOf,
                     Map<String, Employee> mapEmpl) {
        for (String node : root.keySet()) {
            Map listChild = root.get(node);
            for (String child : emplOf.get(node)) {
                mapEmpl.get(child).setSupervisor(node);
                listChild.put(child, new HashMap<>());
            }
            dfs(listChild, emplOf, mapEmpl);
        }
    }
}
