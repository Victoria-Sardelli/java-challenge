package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CacheManager cacheManager;

    private final String EMPLOYEE_CACHE_NAME = "employeeCache";

    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Gets list of all employees stored in database.
     * Also stores employees in cache, if not already there.
     * @return
     */
    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        Cache cache = cacheManager.getCache(EMPLOYEE_CACHE_NAME);
        for (Employee emp : employees) {
            cache.putIfAbsent(emp.getId(), emp);
        }
        return employees;
    }

    /**
     * Gets employee by ID.
     * If employee is not found in cache, then retrieves from database.
     * Returns null if employee is also not found in database.
     * @param employeeId
     * @return
     */
    @Cacheable(value = EMPLOYEE_CACHE_NAME, key = "#employeeId", unless = "#result==null")
    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        return optEmp.orElse(null);
    }

    /**
     * Saves employee to database.
     * Also stores employee in cache.
     * @param employee
     * @return
     */
    @CachePut(value = EMPLOYEE_CACHE_NAME, key = "#result.id")
    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    /**
     * Deletes employee from database.
     * Also removes employee from cache.
     * @param employeeId
     */
    @CacheEvict(value = EMPLOYEE_CACHE_NAME, key = "#employeeId")
    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }

    /**
     * Updates employee stored in database with given ID.
     * Also stores employee in cache with given ID.
     * @param employee
     * @param employeeId
     * @return
     */
    @CachePut(value = EMPLOYEE_CACHE_NAME, key = "#employeeId")
    public Employee updateEmployee(Employee employee, Long employeeId) {
        // set ID so that we update existing entry rather than creating a new one
        employee.setId(employeeId);
        return employeeRepository.save(employee);
    }
}