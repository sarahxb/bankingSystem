import java.sql.*;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String password;
    private String created_at;

    public Customer() {}

    public Customer(int id, String name, String email, String password, String created_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
    }

    public boolean loggingCustomer(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter your email:");
            this.email = scanner.nextLine();
            System.out.println("Enter your password:");
            this.password = scanner.nextLine();

            String encryptedPassword = encrypt(password);
            String query = "SELECT ID_customer, name FROM customer WHERE email = ? AND password = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, encryptedPassword);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.id = rs.getInt("ID_customer");
                this.name = rs.getString("name");
                System.out.println("Login successful! Welcome back, " + name + "!");
                return true;
            } else {
                System.out.println("Invalid email or password.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error during login.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean signUpCustomer(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter your name:");
            this.name = scanner.nextLine();
            System.out.println("Enter your email:");
            this.email = scanner.nextLine();
            System.out.println("Enter your password:");
            this.password = scanner.nextLine();

            String encryptedPassword = encrypt(password);

            String query = "INSERT INTO customer (name, email, password, created_at) VALUES (?, ?, ?, NOW())";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, encryptedPassword);
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                }
                System.out.println("Registration successful! You can now log in.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCreatedAt() { return created_at; }
    public void setCreatedAt(String createdAt) { this.created_at = createdAt; }


    private static final String SECRET_KEY = "1234567890123456";
    public static String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting", e);
        }
    }

    /*public static String decrypt(String strToDecrypt)
    {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting", e);
        }
    }*/

}

