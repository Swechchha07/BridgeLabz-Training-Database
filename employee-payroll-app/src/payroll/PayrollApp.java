package payroll;

import payroll.util.DBUtil;
import payroll.util.HashUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class PayrollApp {

    private static final Scanner sc = new Scanner(System.in);

    private static int currentUserId = -1;
    private static String currentUsername = "";
    private static String currentRole = "";

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n=======================================");
            System.out.println("     EMPLOYEE PAYROLL APPLICATION");
            System.out.println("=======================================");

            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("\nEnter Choice : ");

            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {

                case 1:
                    register();
                    break;

                case 2:
                    login();
                    break;

                case 3:
                    System.out.println("\nThank You...");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice");

            }

        }

    }

    // user register method
    private static void register() {

        System.out.println("\n== REGISTER ==");

        System.out.print("Username : ");
        String username = sc.nextLine();

        System.out.print("Password : ");
        String password = sc.nextLine();

        System.out.print("Email : ");
        String email = sc.nextLine();

        System.out.print("Role (ADMIN/USER) : ");
        String role = sc.nextLine().toUpperCase();

        String sql = "INSERT INTO users(username,password,email,role) VALUES(?,?,?,?)";
        try (
                Connection con = DBUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);

        ) {

            ps.setString(1, username);
            ps.setString(2, HashUtil.hashPassword(password));
            ps.setString(3, email);
            ps.setString(4, role);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("\nRegistration Successful.");

            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Username or Email Already Exists.");

            } else {
                System.out.println(e.getMessage());
            }

        }

    }


    // user login method
    private static void login() {
        System.out.println("\n== LOGIN ==");
        System.out.print("Username : ");
        String username = sc.nextLine();
        System.out.print("Password : ");
        String password = sc.nextLine();

        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (

                Connection con = DBUtil.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

        ) {

            ps.setString(1, username);
            ps.setString(2, HashUtil.hashPassword(password));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                currentUserId = rs.getInt("id");

                currentUsername = rs.getString("username");

                currentRole = rs.getString("role");

                System.out.println("\nWelcome " + currentUsername);
                if (currentRole.equalsIgnoreCase("ADMIN")) {
                    adminMenu();

                } else {

                    userMenu();

                }

            } else {

                System.out.println("Invalid Username or Password.");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // ADMIN MENU

    private static void adminMenu() {

        while (true) {

            System.out.println("\n==================================");
            System.out.println("           ADMIN MENU");
            System.out.println("==================================");

            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Edit Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Department Payroll");
            System.out.println("6. View Audit Logs");
            System.out.println("7. Logout");

            System.out.print("\nEnter Choice : ");

            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {

                case 1:
                    addEmployee();
                    break;

                case 2:
                    viewAllEmployees();
                    break;

                case 3:
                    editEmployee();
                    break;

                case 4:
                    deleteEmployee();
                    break;

                case 5:
                    getDeptPayroll();
                    break;

                case 6:
                    viewAuditLogs();
                    break;

                case 7:

                    logout();
                    return;

                default:
                    System.out.println("Invalid Choice");

            }

        }

    }

    // USER MENU
    private static void userMenu() {

        while (true) {

            System.out.println("\n==================================");
            System.out.println("            USER MENU");
            System.out.println("==================================");

            System.out.println("1. View My Details");
            System.out.println("2. Logout");

            System.out.print("\nEnter Choice : ");

            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {

                case 1:

                    viewMyDetails();

                    break;

                case 2:

                    logout();

                    return;

                default:

                    System.out.println("Invalid Choice");

            }

        }

    }


    // LOGOUT
    private static void logout() {

        currentUserId = -1;
        currentUsername = "";
        currentRole = "";

        System.out.println("\nLogout Successful.");

    }


    // VIEW MY DETAILS
    private static void viewMyDetails() {

        String sql = "SELECT * FROM users WHERE id=?";
        try (

                Connection con = DBUtil.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

        ) {

            ps.setInt(1, currentUserId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                System.out.println("\n=========== PROFILE ===========");

                System.out.println("ID        : "
                        + rs.getInt("id"));

                System.out.println("Username  : "
                        + rs.getString("username"));

                System.out.println("Email     : "
                        + rs.getString("email"));

                System.out.println("Role      : "
                        + rs.getString("role"));

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    // INPUT METHODS
    private static int getIntInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine());

            } catch (Exception e) {
                System.out.println("Enter Valid Integer.");

            }

        }

    }

    private static BigDecimal getDecimalInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return new BigDecimal(sc.nextLine());

            } catch (Exception e) {
                System.out.println("Enter Valid Number.");

            }

        }

    }

// ADD EMPLOYEE
    private static void addEmployee() {

        Connection con = null;

        try {

            con = DBUtil.getConnection();

            con.setAutoCommit(false);

            System.out.println("\n=========== ADD EMPLOYEE ===========");

            System.out.print("Employee Name : ");
            String name = sc.nextLine();

            System.out.print("Profile Image : ");
            String profileImage = sc.nextLine();

            System.out.print("Gender (Male/Female) : ");
            String gender = sc.nextLine();

            BigDecimal salary = getDecimalInput("Salary : ");

            System.out.print("Start Date (yyyy-mm-dd) : ");
            Date startDate = Date.valueOf(sc.nextLine());

            System.out.print("Notes : ");
            String notes = sc.nextLine();

            String employeeSql =
                    "INSERT INTO employees(name,profile_image,gender,salary,start_date,notes,created_by) VALUES(?,?,?,?,?,?,?)";

            PreparedStatement empStmt = con.prepareStatement(employeeSql, Statement.RETURN_GENERATED_KEYS);

            empStmt.setString(1, name);
            empStmt.setString(2, profileImage);
            empStmt.setString(3, gender);
            empStmt.setBigDecimal(4, salary);
            empStmt.setDate(5, startDate);
            empStmt.setString(6, notes);
            empStmt.setInt(7, currentUserId);

            int rows = empStmt.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Employee Insert Failed.");

            }

            ResultSet rs = empStmt.getGeneratedKeys();
            int employeeId = 0;

            if (rs.next()) {
                employeeId = rs.getInt(1);

            }

            int totalDepartments = getIntInput("How Many Departments : ");

            String departmentSql =
                    "INSERT INTO employee_departments(employee_id,department) VALUES(?,?)";

            PreparedStatement deptStmt = con.prepareStatement(departmentSql);

            for (int i = 1; i <= totalDepartments; i++) {

                System.out.print("Department " + i + " : ");

                String department = sc.nextLine();

                deptStmt.setInt(1, employeeId);
                deptStmt.setString(2, department);

                deptStmt.executeUpdate();

            }

            con.commit();

            System.out.println("\nEmployee Added Successfully.");

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                    System.out.println("Transaction Rolled Back.");

                }

            } catch (SQLException ex) {

                e.printStackTrace();

            }

            e.printStackTrace();

        } finally {

            try {

                if (con != null) {

                    con.setAutoCommit(true);

                    con.close();

                }

            } catch (SQLException e) {

                e.printStackTrace();

            }

        }

    }

// VIEW ALL EMPLOYEES
    private static void viewAllEmployees() {

        String sql =
                "SELECT e.id, " +
                        "e.name, " +
                        "e.gender, " +
                        "e.salary, " +
                        "e.start_date, " +
                        "e.notes, " +
                        "u.username, " +
                        "STRING_AGG(ed.department, ', ') AS departments " +
                        "FROM employees e " +
                        "LEFT JOIN employee_departments ed " +
                        "ON e.id = ed.employee_id " +
                        "LEFT JOIN users u " +
                        "ON e.created_by = u.id " +
                        "GROUP BY e.id, u.username " +
                        "ORDER BY e.id";

        try (
                Connection con = DBUtil.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery();

        ) {

            System.out.println("\n=======================================");

            System.out.printf("%-5s %-20s %-10s %-12s %-15s %-25s %-15s %-20s%n",
                    "ID",
                    "NAME",
                    "GENDER",
                    "SALARY",
                    "START DATE",
                    "DEPARTMENTS",
                    "CREATED BY",
                    "NOTES");

            System.out.println("====================================================");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-10s %-12.2f %-15s %-25s %-15s %-20s%n",

                        rs.getInt("id"),

                        rs.getString("name"),

                        rs.getString("gender"),

                        rs.getBigDecimal("salary"),

                        rs.getDate("start_date"),

                        rs.getString("departments"),

                        rs.getString("username"),

                        rs.getString("notes"));

            }

            System.out.println("--------------------------");
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

// EDIT EMPLOYEE
    private static void editEmployee() {

        int id = getIntInput("Enter Employee ID : ");

        String checkSql = "SELECT * FROM employees WHERE id=?";

        try (

                Connection con = DBUtil.getConnection();
                PreparedStatement checkStmt = con.prepareStatement(checkSql);

        ) {

            checkStmt.setInt(1, id);

            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Employee Not Found.");
                return;

            }

            System.out.print("New Name : ");
            String name = sc.nextLine();

            System.out.print("New Profile Image : ");
            String image = sc.nextLine();

            System.out.print("New Gender : ");
            String gender = sc.nextLine();

            BigDecimal salary = getDecimalInput("New Salary : ");

            System.out.print("New Start Date (yyyy-mm-dd) : ");
            Date startDate = Date.valueOf(sc.nextLine());

            System.out.print("New Notes : ");
            String notes = sc.nextLine();

            String updateSql =
                    "UPDATE employees SET name=?,profile_image=?,gender=?,salary=?,start_date=?,notes=? WHERE id=?";

            PreparedStatement ps = con.prepareStatement(updateSql);

            ps.setString(1, name);
            ps.setString(2, image);
            ps.setString(3, gender);
            ps.setBigDecimal(4, salary);
            ps.setDate(5, startDate);
            ps.setString(6, notes);
            ps.setInt(7, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Employee Updated Successfully.");

            } else {

                System.out.println("Update Failed.");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

// DELETE EMPLOYEE
    private static void deleteEmployee() {
        int id = getIntInput("Enter Employee ID : ");
        String sql = "DELETE FROM employees WHERE id=?";

        try (

                Connection con = DBUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);

        ) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Employee Deleted Successfully.");

            } else {

                System.out.println("Employee Not Found.");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


// DEPARTMENT PAYROLL
    private static void getDeptPayroll() {

        System.out.print("Enter Department Name : ");
        String department = sc.nextLine();

        String sql = "{ ? = call get_total_payroll_by_dept(?) }";

        try (
                Connection con = DBUtil.getConnection();
                CallableStatement cs = con.prepareCall(sql);
        ) {

            cs.registerOutParameter(1, Types.NUMERIC);

            cs.setString(2, department);

            cs.execute();

            BigDecimal totalPayroll = cs.getBigDecimal(1);

            System.out.println("\nDepartment : " + department);
            System.out.println("Total Payroll : ₹" + totalPayroll);

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }


// VIEW AUDIT LOGS
    private static void viewAuditLogs() {
        String sql = "SELECT * FROM payroll_audit ORDER BY changed_at DESC";
        try (

                Connection con = DBUtil.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

        ) {

            System.out.println();

            System.out.println("======================================================");

            System.out.printf("%-5s %-10s %-10s %-12s %-12s %-15s %-20s%n",

                    "ID",

                    "EMP ID",

                    "ACTION",

                    "OLD",

                    "NEW",

                    "CHANGED BY",

                    "DATE");

            System.out.println("==================================================");

            while (rs.next()) {

                System.out.printf("%-5d %-10d %-10s %-12s %-12s %-15s %-20s%n",

                        rs.getInt("id"),

                        rs.getInt("employee_id"),

                        rs.getString("action_type"),

                        rs.getBigDecimal("old_salary"),

                        rs.getBigDecimal("new_salary"),

                        rs.getString("changed_by"),

                        rs.getTimestamp("changed_at"));

            }

            System.out.println("=====================");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}