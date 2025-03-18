

import java.sql.*;
import java.util.Date;

public class Conexiune{
    private static Connection connection;

    public Conexiune() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.err.println("An Exception occured during JDBC Driver loading." +
                    " Details are provided below:");
            ex.printStackTrace(System.err);
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/bank_system?user=root&password=SarahDaniel11");
        } catch (SQLException sqlex) {
            System.err.println("An SQL Exception occured. Details are provided below:");
            sqlex.printStackTrace(System.err);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}