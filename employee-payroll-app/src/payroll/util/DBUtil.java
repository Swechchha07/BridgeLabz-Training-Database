package payroll.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {


    private static final String URL = "jdbc:postgresql://localhost:5432/payroll_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2407";

    public static Connection getConnection() throws SQLException {

        try {
            // Load PostgreSQL Driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver Not Found!");
            e.printStackTrace();
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}