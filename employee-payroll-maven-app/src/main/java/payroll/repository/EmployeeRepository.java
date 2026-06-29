package payroll.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import payroll.model.Employee;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    // Re-usable SELECT that pulls an employee together with the
    // comma joined department list and the admin who created it.
    private static final String SELECT_EMPLOYEE =
            "SELECT e.id, " +
                    "e.name, " +
                    "e.profile_image, " +
                    "e.gender, " +
                    "e.salary, " +
                    "e.start_date, " +
                    "e.notes, " +
                    "e.created_by, " +
                    "STRING_AGG(ed.department, ', ') AS departments " +
                    "FROM employees e " +
                    "LEFT JOIN employee_departments ed " +
                    "ON e.id = ed.employee_id ";

    // Turns one joined row into an Employee object.
    // The aggregated department string is split back into a list.
    private final RowMapper<Employee> employeeRowMapper = (rs, rowNum) -> {

        Employee emp = new Employee();

        emp.setId(rs.getInt("id"));
        emp.setName(rs.getString("name"));
        emp.setProfileImage(rs.getString("profile_image"));
        emp.setGender(rs.getString("gender"));
        emp.setSalary(rs.getBigDecimal("salary"));

        Date startDate = rs.getDate("start_date");

        if (startDate != null) {

            emp.setStartDate(startDate.toLocalDate());

        }

        emp.setNotes(rs.getString("notes"));
        emp.setCreatedBy(rs.getInt("created_by"));

        String departments = rs.getString("departments");

        if (departments != null) {

            emp.setDepartments(Arrays.asList(departments.split(", ")));

        } else {

            emp.setDepartments(new ArrayList<>());

        }

        return emp;

    };

    //====================================================
    // ADD EMPLOYEE
    //====================================================

    public void addEmployee(Employee emp) {

        // Both inserts must succeed together, so we wrap them
        // in a single programmatic transaction.
        transactionTemplate.execute(status -> {

            String employeeSql =
                    "INSERT INTO employees(name,profile_image,gender,salary,start_date,notes,created_by) VALUES(?,?,?,?,?,?,?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {

                PreparedStatement ps =
                        connection.prepareStatement(employeeSql,
                                Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, emp.getName());
                ps.setString(2, emp.getProfileImage());
                ps.setString(3, emp.getGender());
                ps.setBigDecimal(4, emp.getSalary());
                ps.setDate(5, Date.valueOf(emp.getStartDate()));
                ps.setString(6, emp.getNotes());
                ps.setInt(7, emp.getCreatedBy());

                return ps;

            }, keyHolder);

            int employeeId =
                    keyHolder.getKey().intValue();

            String departmentSql =
                    "INSERT INTO employee_departments(employee_id,department) VALUES(?,?)";

            for (String department : emp.getDepartments()) {

                jdbcTemplate.update(departmentSql, employeeId, department);

            }

            return null;

        });

    }

    //====================================================
    // FIND ALL
    //====================================================

    public List<Employee> findAll() {

        String sql =
                SELECT_EMPLOYEE +
                        "GROUP BY e.id " +
                        "ORDER BY e.id";

        return jdbcTemplate.query(sql, employeeRowMapper);

    }

    //====================================================
    // FIND BY ID
    //====================================================

    public Employee findById(int id) {

        String sql =
                SELECT_EMPLOYEE +
                        "WHERE e.id = ? " +
                        "GROUP BY e.id";

        List<Employee> list =
                jdbcTemplate.query(sql, employeeRowMapper, id);

        if (list.isEmpty()) {

            return null;

        }

        return list.get(0);

    }

    //====================================================
    // FIND BY EMAIL
    //====================================================

    public Employee findByEmail(String email) {

        String sql =
                SELECT_EMPLOYEE +
                        "LEFT JOIN users u ON e.created_by = u.id " +
                        "WHERE u.email = ? " +
                        "GROUP BY e.id";

        List<Employee> list =
                jdbcTemplate.query(sql, employeeRowMapper, email);

        if (list.isEmpty()) {

            return null;

        }

        return list.get(0);

    }

    //====================================================
    // UPDATE EMPLOYEE
    //====================================================

    public void updateEmployee(int id,
                               BigDecimal salary,
                               String notes,
                               int adminId) {

        String sql =
                "UPDATE employees SET salary=?,notes=?,created_by=? WHERE id=?";

        jdbcTemplate.update(sql, salary, notes, adminId, id);

    }

    //====================================================
    // DELETE EMPLOYEE
    //====================================================

    public void deleteEmployee(int id) {

        String sql =
                "DELETE FROM employees WHERE id=?";

        jdbcTemplate.update(sql, id);

    }

    //====================================================
    // DEPARTMENT PAYROLL
    //====================================================

    public BigDecimal getDeptPayroll(String dept) {

        String sql =
                "SELECT get_total_payroll_by_dept(?)";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, dept);

    }

    //====================================================
    // AUDIT LOGS
    //====================================================

    public List<Map<String, Object>> findAuditLogs() {

        String sql =
                "SELECT * FROM payroll_audit ORDER BY changed_at DESC";

        return jdbcTemplate.queryForList(sql);

    }

}
