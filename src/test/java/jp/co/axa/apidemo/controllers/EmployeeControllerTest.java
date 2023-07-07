package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;

import static jp.co.axa.apidemo.TestUtil.*;
import static org.hamcrest.core.Is.is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    private String baseApiUrl = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    /**
     * Tests that getEmployees() returns OK status and list of employees upon successful retrieval
     * @throws Exception
     */
    @Test
    public void shouldReturnEmployees() throws Exception {
        // mock service will return list of employees
        Employee employee = getTestEmployee();
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        doReturn(employees).when(employeeService).retrieveEmployees();

        // confirm that response contains list of employees
        mockMvc.perform(get(baseApiUrl + "/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(employee.getName())));
    }

    /**
     * Tests that getEmployee() returns OK status and employee information upon successful retrieval
     * @throws Exception
     */
    @Test
    public void shouldGetEmployee() throws Exception {
        Employee employee = getTestEmployee();
        doReturn(employee).when(employeeService).getEmployee(employee.getId());
        mockMvc.perform(get(baseApiUrl + "/employees/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(employee.getName())));
    }

    /**
     * Tests that getEmployee() returns Not Found status and error message when employee does not exist in database
     * @throws Exception
     */
    @Test
    public void shouldReturnErrorMessageForGetEmployee() throws Exception {
        long id = 1L;
        doReturn(null).when(employeeService).getEmployee(id);

        mockMvc.perform(get(baseApiUrl + "/employees/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Employee not found - ID: " + id)));
    }

    /**
     * Tests that saveEmployee() returns OK status and success message when employee is successfully saved
     * @throws Exception
     */
    @Test
    public void shouldSaveEmployee() throws Exception {
        Employee employee = getTestEmployee();
        doReturn(employee).when(employeeService).saveEmployee(employee);
        mockMvc.perform(post(baseApiUrl + "/employees")
                        .contentType(APPLICATION_JSON)
                        .content(asJson(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("Employee saved successfully")));
    }

    /**
     * Tests that deleteEmployee() returns OK status and success message when employee is successfully deleted
     * @throws Exception
     */
    @Test
    public void shouldDeleteEmployee() throws Exception {
        Employee employee = getTestEmployee();
        doNothing().when(employeeService).deleteEmployee(any());
        mockMvc.perform(delete(baseApiUrl + "/employees/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("Employee deleted successfully - ID: " + employee.getId())));
    }

    /**
     * Tests that updateEmployee returns OK status and success message when employee is successfully updated
     * @throws Exception
     */
    @Test
    public void shouldUpdateEmployee() throws Exception {
        Employee employee = getTestEmployee();
        doReturn(employee).when(employeeService).getEmployee(employee.getId());
        doReturn(employee).when(employeeService).updateEmployee(employee, employee.getId());
        mockMvc.perform(
                put(baseApiUrl + "/employees/" + employee.getId()).content(asJson(employee)).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("Employee updated successfully - ID: " + employee.getId())));
    }

    /**
     * Tests that updateEmployee() returns Not Found status and error message
     * when employee to update does not exist in database
     * @throws Exception
     */
    @Test
    public void shouldReturnErrorMessageForUpdateEmployee() throws Exception {
        long id = 1L;
        doReturn(null).when(employeeService).getEmployee(id);

        mockMvc.perform(get(baseApiUrl + "/employees/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Employee not found - ID: " + id)));
    }
}
