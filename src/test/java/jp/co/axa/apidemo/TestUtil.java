package jp.co.axa.apidemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.Employee;

public final class TestUtil {

    private TestUtil() {}

    /**
     * Creates employee object with dummy data that can be used for tests
     * @return Employee
     */
    public static Employee getTestEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Name");
        employee.setSalary(1);
        employee.setDepartment("Department");
        return employee;
    }

    /**
     * Converts object to JSON string
     * @param obj
     * @return String representing JSON for given object
     */
    public static String asJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
