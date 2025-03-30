
import java.sql.*;
public class Account {
    private int id_account;
    private int id_customer;
    private float balance;
    private String created_at;

    public Account(){};
    public Account(int id_account, int id_customer, float balance, String created_at) {
        this.id_account = id_account;
        this.id_customer = id_customer;
        this.balance = balance;
        this.created_at = created_at;
    }
    public boolean createAccount(Connection connection, int id_customer, float initialBalance) {
        try {
            String query = "INSERT INTO account (id_customer, balance, created_at) VALUES (?, ?, NOW())";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id_customer);
            preparedStatement.setFloat(2, initialBalance);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Account successfully created!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void viewAccounts(Connection connection, int customerId) {
        try {
            String query = "SELECT * FROM account WHERE id_customer = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean hasAccounts = false;
            System.out.println("\nYour Accounts:");
            System.out.println("--------------------------------------");

            while (resultSet.next()) {
                hasAccounts = true;
                int accountId = resultSet.getInt("id_account");
                float balance = resultSet.getFloat("balance");
                String createdAt = resultSet.getString("created_at");

                System.out.println("Account ID: " + accountId);
                System.out.println("Balance: $" + balance);
               // System.out.println("Created At: " + createdAt);
                System.out.println("--------------------------------------");
            }

            if (!hasAccounts) {
                System.out.println("No accounts found for this customer.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deposit(Connection connection, int id_account, float amount,int id_customer) {
        try {
            // Check if account exists
            String checkQuery = "SELECT balance FROM account WHERE id_account = ? AND id_customer = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, id_account);
            checkStmt.setInt(2, id_customer);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                float currentBalance = resultSet.getFloat("balance");

                // Update balance
                String updateQuery = "UPDATE account SET balance = ? WHERE id_account = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setFloat(1, currentBalance + amount);
                updateStmt.setInt(2, id_account);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Deposit successful! New balance: $" + (currentBalance + amount));
                } else {
                    System.out.println("Deposit failed.");
                }
            } else {
                System.out.println("Account not found or does not belong to the customer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void withdrawal(Connection connection, int id_account, float amount,int id_customer) {
        try {
            // Check if account exists
            String checkQuery = "SELECT balance FROM account WHERE id_account = ? AND id_customer = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, id_account);
            checkStmt.setInt(2, id_customer);
            ResultSet resultSet = checkStmt.executeQuery();


            if (resultSet.next()) {
                float currentBalance = resultSet.getFloat("balance");
                if (amount <= currentBalance) {

                // Update balance
                String updateQuery = "UPDATE account SET balance = ? WHERE id_account = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setFloat(1, currentBalance - amount);
                updateStmt.setInt(2, id_account);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Withdrawal successful! New balance: $" + (currentBalance - amount));
                } else {
                    System.out.println("Withdrawal failed.");
                }
            }
                else
                    System.out.println("Withdrawal failed.Insufficient funds.");

            } else {
                System.out.println("Account not found or does not belong to the customer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public int getIdAccount() { return id_account; }
    public void setIdAccount(int id_account) { this.id_account = id_account; }

    public int getIdCustomer() { return id_customer; }
    public void setIdCustomer(int id_customer) { this.id_customer = id_customer; }

    public float getBalance() { return balance; }
    public void setBalance(float balance) { this.balance = balance; }

    public String getCreatedAt() { return created_at; }
    public void setCreatedAt(String created_at) { this.created_at = created_at; }

    public void viewCustomerAccounts(Connection connection, int id_customer) {
        String query = "SELECT id_account, balance FROM account WHERE id_customer = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id_customer);
            ResultSet resultSet = stmt.executeQuery();

            System.out.println("Your Accounts:");
            System.out.println("---------------------");
            while (resultSet.next()) {
                int id_account = resultSet.getInt("id_account");
                float balance = resultSet.getFloat("balance");
                System.out.println("Account ID: " + id_account + " | Balance: $" + balance);
            }
            System.out.println("---------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving accounts.");
            e.printStackTrace();
        }
    }

    public void transactionHistory(Connection connection, int id_account) {
        String query = "SELECT * FROM transaction_history WHERE id_account = ?";



    }
}


