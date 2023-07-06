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
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        Employee emp = employeeService.getEmployee(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found - ID: " + employeeId));
        return ResponseEntity.ok(emp);
    }

    @PostMapping("/employees")
    public ResponseEntity<String> saveEmployee(Employee employee){
        employeeService.saveEmployee(employee);
        String successMsg = "Employee saved successfully";
        logger.info(successMsg);
        return ResponseEntity.ok(successMsg);
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        employeeService.deleteEmployee(employeeId);
        String successMsg = "Employee deleted successfully - ID: " + employeeId;
        logger.info(successMsg);
        return ResponseEntity.ok(successMsg);
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        // check if employee exists prior to attempting update
        employeeService.getEmployee(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found - ID: " + employeeId));
        employeeService.updateEmployee(employee, employeeId);
        String successMsg = "Employee updated successfully - ID: " + employeeId;
        logger.info(successMsg);
        return ResponseEntity.ok(successMsg);
    }

}
