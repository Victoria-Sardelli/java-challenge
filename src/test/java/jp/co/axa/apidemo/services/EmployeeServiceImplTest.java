package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jp.co.axa.apidemo.TestUtil.getTestEmployee;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeServiceImpl.class)
public class EmployeeServiceImplTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public EmployeeService employeeService() {
            return new EmployeeServiceImpl();
        }
    }

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    // captor used to check value of arguments passed to methods of mocked objects
    @Captor
    ArgumentCaptor<Employee> captor;

    /**
     * Tests that retrieveEmployees() returns list of employees retrieved from repository
     */
    @Test
    public void shouldRetrieveEmployees() {
        Employee employee = getTestEmployee();
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        doReturn(employees).when(employeeRepository).findAll();

        List<Employee> returnedList = employeeService.retrieveEmployees();
        assertThat(returnedList).isEqualTo(employees);
    }

    /**
     * Tests that getEmployee() returns employee retrieved from repository
     */
    @Test
    public void shouldGetEmployee() {
        Employee employee = getTestEmployee();
        doReturn(Optional.of(employee)).when(employeeRepository).findById(employee.getId());

        Employee emp = employeeService.getEmployee(employee.getId());
        assertThat(emp).isEqualTo(employee);
    }

    /**
     * Tests that getEmployee() returns null when repository does not contain requested employee
     */
    @Test
    public void shouldReturnNullIfNoEmployee() {
        Employee employee = getTestEmployee();
        doReturn(Optional.empty()).when(employeeRepository).findById(employee.getId());

        Employee emp = employeeService.getEmployee(employee.getId());
        assertThat(emp).isNull();
    }

    /**
     * Tests that saveEmployee() saves employee data to repository
     */
    @Test
    public void shouldSaveEmployee() {
        Employee employee = getTestEmployee();
        doReturn(employee).when(employeeRepository).save(employee);

        Employee returnedEmployee = employeeService.saveEmployee(employee);
        verify(employeeRepository, times(1)).save(employee);
        assertThat(returnedEmployee).isEqualTo(employee);
    }

    /**
     * Tests that deleteEmployee() deletes employee with given ID from repository
     */
    @Test
    public void shouldDeleteEmployee() {
        Employee employee = getTestEmployee();
        doNothing().when(employeeRepository).deleteById(employee.getId());

        employeeService.deleteEmployee(employee.getId());
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }

    /**
     * Tests that updateEmployee() saves employee data to repository using given ID
     */
    @Test
    public void shouldUpdateEmployee() {
        // test employee data to save with test id
        Employee employee = getTestEmployee();
        Long testId = 100L;

        // test employee that represents expected state of entry after it is updated (should have given test id)
        Employee employeeWithTestId = getTestEmployee();
        employeeWithTestId.setId(testId);
        doReturn(employeeWithTestId).when(employeeRepository).save(employee);

        Employee returnedEmployee = employeeService.updateEmployee(employee, testId);

        // confirm that the service provided repository with correct id and employee data when attempting to save
        verify(employeeRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(testId);
        assertThat(captor.getValue().getName()).isEqualTo(employee.getName());

        // confirm that the returned employee object contains the same data as what was provided to repository
        assertThat(returnedEmployee.getId()).isEqualTo(testId);
        assertThat(returnedEmployee.getName()).isEqualTo(employee.getName());
    }
}
