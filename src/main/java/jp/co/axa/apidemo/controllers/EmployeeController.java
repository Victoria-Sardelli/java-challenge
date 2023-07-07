package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EntityNotFoundException;
import jp.co.axa.apidemo.message.ResponseMessage;
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

    /**
     * Gets list of all employees
     * @return
     */
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * Gets one employee by ID. If no employee currently exists with given ID, throws error
     * @param employeeId
     * @return
     */
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        Employee emp = employeeService.getEmployee(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found - ID: " + employeeId));
        return ResponseEntity.ok(emp);
    }

    /**
     * Stores new employee entry in database
     * @param employee
     * @return
     */
    @PostMapping("/employees")
    public ResponseEntity<ResponseMessage> saveEmployee(@RequestBody Employee employee){
        employeeService.saveEmployee(employee);
        String successMsg = "Employee saved successfully";
        logger.info(successMsg);
        return ResponseEntity.ok(new ResponseMessage(successMsg));
    }

    /**
     * Deletes one employee by ID
     * @param employeeId
     * @return
     */
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<ResponseMessage> deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        employeeService.deleteEmployee(employeeId);
        String successMsg = "Employee deleted successfully - ID: " + employeeId;
        logger.info(successMsg);
        return ResponseEntity.ok(new ResponseMessage(successMsg));
    }

    /**
     * Gets one employee by ID and overwrites their data with contents of request body.
     * If no employee currently exists with given ID, throws error
     * @param employee
     * @param employeeId
     * @return
     */
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<ResponseMessage> updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        // check if employee exists prior to attempting update
        employeeService.getEmployee(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found - ID: " + employeeId));
        employeeService.updateEmployee(employee, employeeId);
        String successMsg = "Employee updated successfully - ID: " + employeeId;
        logger.info(successMsg);
        return ResponseEntity.ok(new ResponseMessage(successMsg));
    }

}
