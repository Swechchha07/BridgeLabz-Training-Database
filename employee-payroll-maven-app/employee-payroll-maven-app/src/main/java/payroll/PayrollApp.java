package payroll;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DuplicateKeyException;

import payroll.config.AppConfig;
import payroll.model.Employee;
import payroll.model.User;
import payroll.repository.EmployeeRepository;
import payroll.repository.UserRepository;
import payroll.util.HashUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PayrollApp {

    private static User currentUser = null;

    private static final Scanner scanner = new Scanner(System.in);

    private static final List<String> AVAILABLE_PROFILES =
            Arrays.asList(
                    "ellipse-1.png",
                    "ellipse-2.png",
                    "ellipse-3.png",
                    "ellipse-4.png",
                    "ellipse-5.png");

    private static UserRepository userRepository;

    private static EmployeeRepository employeeRepository;

    public static void main(String[] args) {

        // Load the .env file into system properties so that the
        // ${...} placeholders inside application.properties resolve.
        loadEnv();

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        userRepository = context.getBean(UserRepository.class);

        employeeRepository = context.getBean(EmployeeRepository.class);

        while (true) {

            if (currentUser == null) {

                showAnonymousMenu();

            } else if (currentUser.getRole().equalsIgnoreCase("ADMIN")) {

                showAdminMenu();

            } else {

                showUserMenu();

            }

        }

    }

    //====================================================
    // ENV LOADER
    //====================================================

    private static void loadEnv() {

        // The working directory is not always the project root (IntelliJ may
        // start the app from a different folder), so we look for the .env file
        // in the current directory and then walk up through the parent folders.
        File envFile = locateEnvFile();

        if (envFile == null) {

            System.out.println("Could not find .env file. Looked in : "
                    + System.getProperty("user.dir"));

            return;

        }

        try (BufferedReader br = new BufferedReader(new FileReader(envFile))) {

            String line;

            while ((line = br.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {

                    continue;

                }

                int index = line.indexOf('=');

                if (index > 0) {

                    String key = line.substring(0, index).trim();

                    String value = line.substring(index + 1).trim();

                    System.setProperty(key, value);

                }

            }

        } catch (Exception e) {

            System.out.println("Could not read .env file : " + e.getMessage());

        }

    }

    // Searches the current working directory and its parent folders for a .env file.
    private static File locateEnvFile() {

        File dir = new File(System.getProperty("user.dir"));

        while (dir != null) {

            File candidate = new File(dir, ".env");

            if (candidate.exists()) {

                return candidate;

            }

            dir = dir.getParentFile();

        }

        return null;

    }

    //====================================================
    // ANONYMOUS MENU
    //====================================================

    private static void showAnonymousMenu() {

        System.out.println("\n=======================================");
        System.out.println("     EMPLOYEE PAYROLL APPLICATION");
        System.out.println("=======================================");

        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");

        System.out.print("\nEnter Choice : ");

        int choice = getIntInput();

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

    //====================================================
    // ADMIN MENU
    //====================================================

    private static void showAdminMenu() {

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

        int choice = getIntInput();

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
                break;

            default:
                System.out.println("Invalid Choice");

        }

    }

    //====================================================
    // USER MENU
    //====================================================

    private static void showUserMenu() {

        System.out.println("\n==================================");
        System.out.println("            USER MENU");
        System.out.println("==================================");

        System.out.println("1. View My Details");
        System.out.println("2. Logout");

        System.out.print("\nEnter Choice : ");

        int choice = getIntInput();

        switch (choice) {

            case 1:
                viewMyDetails();
                break;

            case 2:
                logout();
                break;

            default:
                System.out.println("Invalid Choice");

        }

    }

    //====================================================
    // REGISTER
    //====================================================

    private static void register() {

        System.out.println("\n========= REGISTER =========");

        System.out.print("Username : ");
        String username = scanner.nextLine();

        System.out.print("Password : ");
        String password = scanner.nextLine();

        System.out.print("Email : ");
        String email = scanner.nextLine();

        System.out.print("Role (ADMIN/USER) : ");
        String role = scanner.nextLine().toUpperCase();

        try {

            userRepository.registerUser(username,
                    HashUtil.hashPassword(password),
                    email,
                    role);

            System.out.println("\nRegistration Successful.");

        } catch (DuplicateKeyException e) {

            System.out.println("Username or Email Already Exists.");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    //====================================================
    // LOGIN
    //====================================================

    private static void login() {

        System.out.println("\n========= LOGIN =========");

        System.out.print("Username : ");
        String username = scanner.nextLine();

        System.out.print("Password : ");
        String password = scanner.nextLine();

        User user = userRepository.findByUsername(username);

        if (user != null
                && user.getPassword().equals(HashUtil.hashPassword(password))) {

            currentUser = user;

            System.out.println("\nWelcome " + currentUser.getUsername());

        } else {

            System.out.println("Invalid Username or Password.");

        }

    }

    //====================================================
    // LOGOUT
    //====================================================

    private static void logout() {

        currentUser = null;

        System.out.println("\nLogout Successful.");

    }

    //====================================================
    // VIEW MY DETAILS
    //====================================================

    private static void viewMyDetails() {

        User user =
                userRepository.findByUsername(currentUser.getUsername());

        if (user != null) {

            System.out.println("\n=========== PROFILE ===========");

            System.out.println("ID        : " + user.getId());

            System.out.println("Username  : " + user.getUsername());

            System.out.println("Email     : " + user.getEmail());

            System.out.println("Role      : " + user.getRole());

        }

    }

    //====================================================
    // ADD EMPLOYEE
    //====================================================

    private static void addEmployee() {

        System.out.println("\n=========== ADD EMPLOYEE ===========");

        System.out.print("Employee Name : ");
        String name = scanner.nextLine();

        System.out.println("\nAvailable Profile Images :");

        for (int i = 0; i < AVAILABLE_PROFILES.size(); i++) {

            System.out.println((i + 1) + ". " + AVAILABLE_PROFILES.get(i));

        }

        System.out.print("Select Profile Image : ");
        int profileChoice = getIntInput();

        String profileImage;

        if (profileChoice >= 1 && profileChoice <= AVAILABLE_PROFILES.size()) {

            profileImage = AVAILABLE_PROFILES.get(profileChoice - 1);

        } else {

            profileImage = AVAILABLE_PROFILES.get(0);

        }

        System.out.print("Gender (Male/Female) : ");
        String gender = scanner.nextLine();

        System.out.print("Salary : ");
        BigDecimal salary = getDecimalInput();

        System.out.print("Start Date (yyyy-mm-dd) : ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Notes : ");
        String notes = scanner.nextLine();

        System.out.print("How Many Departments : ");
        int totalDepartments = getIntInput();

        List<String> departments = new ArrayList<>();

        for (int i = 1; i <= totalDepartments; i++) {

            System.out.print("Department " + i + " : ");

            departments.add(scanner.nextLine());

        }

        Employee emp = new Employee();

        emp.setName(name);
        emp.setProfileImage(profileImage);
        emp.setGender(gender);
        emp.setDepartments(departments);
        emp.setSalary(salary);
        emp.setStartDate(startDate);
        emp.setNotes(notes);
        emp.setCreatedBy(currentUser.getId());

        try {

            employeeRepository.addEmployee(emp);

            System.out.println("\nEmployee Added Successfully.");

        } catch (Exception e) {

            System.out.println("Transaction Rolled Back.");

            System.out.println(e.getMessage());

        }

    }

    //====================================================
    // VIEW ALL EMPLOYEES
    //====================================================

    private static void viewAllEmployees() {

        List<Employee> employees = employeeRepository.findAll();

        System.out.println("\n===================================================================================================================");

        System.out.printf("%-5s %-20s %-10s %-12s %-15s %-25s %-12s %-20s%n",
                "ID",
                "NAME",
                "GENDER",
                "SALARY",
                "START DATE",
                "DEPARTMENTS",
                "CREATED BY",
                "NOTES");

        System.out.println("===================================================================================================================");

        for (Employee emp : employees) {

            System.out.printf("%-5d %-20s %-10s %-12.2f %-15s %-25s %-12d %-20s%n",

                    emp.getId(),

                    emp.getName(),

                    emp.getGender(),

                    emp.getSalary(),

                    emp.getStartDate(),

                    String.join(", ", emp.getDepartments()),

                    emp.getCreatedBy(),

                    emp.getNotes());

        }

        System.out.println("--------------------------");

    }

    //====================================================
    // EDIT EMPLOYEE
    //====================================================

    private static void editEmployee() {

        System.out.print("Enter Employee ID : ");
        int id = getIntInput();

        Employee emp = employeeRepository.findById(id);

        if (emp == null) {

            System.out.println("Employee Not Found.");

            return;

        }

        System.out.print("New Salary : ");
        BigDecimal salary = getDecimalInput();

        System.out.print("New Notes : ");
        String notes = scanner.nextLine();

        try {

            employeeRepository.updateEmployee(id,
                    salary,
                    notes,
                    currentUser.getId());

            System.out.println("Employee Updated Successfully.");

        } catch (Exception e) {

            System.out.println("Update Failed.");

            System.out.println(e.getMessage());

        }

    }

    //====================================================
    // DELETE EMPLOYEE
    //====================================================

    private static void deleteEmployee() {

        System.out.print("Enter Employee ID : ");
        int id = getIntInput();

        Employee emp = employeeRepository.findById(id);

        if (emp == null) {

            System.out.println("Employee Not Found.");

            return;

        }

        employeeRepository.deleteEmployee(id);

        System.out.println("Employee Deleted Successfully.");

    }

    //====================================================
    // DEPARTMENT PAYROLL
    //====================================================

    private static void getDeptPayroll() {

        System.out.print("Enter Department Name : ");
        String department = scanner.nextLine();

        BigDecimal totalPayroll =
                employeeRepository.getDeptPayroll(department);

        System.out.println("\nDepartment : " + department);
        System.out.println("Total Payroll : " + totalPayroll);

    }

    //====================================================
    // VIEW AUDIT LOGS
    //====================================================

    private static void viewAuditLogs() {

        List<Map<String, Object>> logs =
                employeeRepository.findAuditLogs();

        System.out.println();

        System.out.println("==============================================================================================");

        System.out.printf("%-5s %-10s %-10s %-12s %-12s %-15s %-20s%n",
                "ID",
                "EMP ID",
                "ACTION",
                "OLD",
                "NEW",
                "CHANGED BY",
                "DATE");

        System.out.println("==============================================================================================");

        for (Map<String, Object> row : logs) {

            System.out.printf("%-5s %-10s %-10s %-12s %-12s %-15s %-20s%n",

                    row.get("id"),

                    row.get("employee_id"),

                    row.get("action_type"),

                    row.get("old_salary"),

                    row.get("new_salary"),

                    row.get("changed_by"),

                    row.get("changed_at"));

        }

        System.out.println("=====================");

    }

    //====================================================
    // INPUT METHODS
    //====================================================

    private static int getIntInput() {

        while (true) {

            try {

                return Integer.parseInt(scanner.nextLine());

            } catch (Exception e) {

                System.out.print("Enter Valid Integer : ");

            }

        }

    }

    private static BigDecimal getDecimalInput() {

        while (true) {

            try {

                return new BigDecimal(scanner.nextLine());

            } catch (Exception e) {

                System.out.print("Enter Valid Number : ");

            }

        }

    }

}
