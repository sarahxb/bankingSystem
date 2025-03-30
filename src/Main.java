
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.*;



public class Main {


    public static void main(String[] args) {


        Customer customer=new Customer();
        Account account=new Account();
        Menu menu=new Menu();


        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bank_system",
                    "root", "SarahDaniel11.");


                menu.choiceA(customer,connection,account);

                connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Add it to the classpath!");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //scanner.close();
    }




}
