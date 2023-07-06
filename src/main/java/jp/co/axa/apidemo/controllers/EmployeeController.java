package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EntityNotFoundException;
import jp.co.axa.apidemo.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        Employee emp = employeeService.getEmployee(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee Not Found - ID: " + employeeId));
        return ResponseEntity.ok(emp);
    }

    @PostMapping("/employees")
    public void saveEmployee(Employee employee){
        employeeService.saveEmployee(employee);
        logger.info("Employee Saved Successfully");
    }

    @DeleteMapping("/employees/{employeeId}")
    public void deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        employeeService.deleteEmployee(employeeId);
        logger.info("Employee Deleted Successfully - ID: " + employeeId);
    }

    @PutMapping("/employees/{employeeId}")
    public void updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        // check if employee exists prior to attempting update
        employeeService.getEmployee(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee Not Found - ID: " + employeeId));
        employeeService.updateEmployee(employee, employeeId);
        logger.info("Employee Updated Successfully - ID: " + employeeId);
    }

}
