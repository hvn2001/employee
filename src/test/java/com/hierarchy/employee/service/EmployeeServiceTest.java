package com.hierarchy.employee.service;

import com.hierarchy.employee.model.Employee;
import com.hierarchy.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void shouldReturnListOfEmployeeDtoWhenGetEmployeesCalled() {
        List<Employee> employees = new ArrayList<>();
        Employee employee = getEmployee1();
        employees.add(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        List<Employee> employeeDtos = employeeService.getEmployees();
        assertThat(1).isEqualTo(employeeDtos.size());
        assertThat(employeeDtos.get(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "developer")
                .hasFieldOrPropertyWithValue("supervisor", "lead");
    }

    @Test
    void shouldPostHierarchy() {
        Map<String, String> input = new HashMap<>();
        input.put("Pete", "Nick");
        input.put("Barbara", "Nick");
        input.put("Nick", "Sophie");
        input.put("backend", "Barbara");
        input.put("frontend", "Pete");
        input.put("Sophie", "Jonas");

        Map<String, Map> output = employeeService.postHierarchy(input);
        employeeRepository.findByName("HV");
        /*
        {
            "Jonas": {
                "Sophie": {
                    "Nick": {
                        "Pete": {
                            "frontend": {}
                        },
                        "Barbara": {
                            "backend": {}
                        }
                    }
                }
            }
        }
        * */
        Assertions.assertTrue(output.containsKey("Jonas"));
        Map layer2 = output.get("Jonas");
        Assertions.assertTrue(layer2.containsKey("Sophie"));
        Map layer3 = (Map) layer2.get("Sophie");
        Assertions.assertTrue(layer3.containsKey("Nick"));
        Map layer4 = (Map) layer3.get("Nick");
        Assertions.assertEquals(2, layer4.size());
        Assertions.assertTrue(layer4.containsKey("Pete"));
        Assertions.assertTrue(layer4.containsKey("Barbara"));

        Map layer51 = (Map) layer4.get("Pete");
        Assertions.assertTrue(layer51.containsKey("frontend"));
        Map layer52 = (Map) layer4.get("Barbara");
        Assertions.assertTrue(layer52.containsKey("backend"));

    }

    private Employee getEmployee1() {
        return Employee.builder()
                .name("developer")
                .supervisor("lead")
                .id(UUID.randomUUID())
                .build();
    }
}
