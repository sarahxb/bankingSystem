
import java.sql.*;


public class Transfer {

    public boolean executeTransfer(Connection connection, int from_account, int to_account, float amount) {
        try {
            connection.setAutoCommit(false); // Start transaction

            // 1. Check if fromAccount exists and has enough balance
            String balanceQuery = "SELECT balance FROM account WHERE id_account = ?";
            PreparedStatement balanceStmt = connection.prepareStatement(balanceQuery);
            balanceStmt.setInt(1, from_account);
            ResultSet resultSet = balanceStmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Source account does not exist.");
                return false;
            }

            float currentBalance = resultSet.getFloat("balance");
            if (currentBalance < amount) {
                System.out.println("Insufficient funds.");
                return false;
            }

            // 2. Deduct amount from fromAccount
            String deductQuery = "UPDATE account SET balance = balance - ? WHERE id_account = ?";
            PreparedStatement deductStmt = connection.prepareStatement(deductQuery);
            deductStmt.setFloat(1, amount);
            deductStmt.setInt(2, from_account);
            deductStmt.executeUpdate();

            // 3. Add amount to toAccount
            String addQuery = "UPDATE account SET balance = balance + ? WHERE id_account = ?";
            PreparedStatement addStmt = connection.prepareStatement(addQuery);
            addStmt.setFloat(1, amount);
            addStmt.setInt(2, to_account);
            addStmt.executeUpdate();

            // 4. Record the transfer
            String transferQuery = "INSERT INTO transfer (from_account, to_account, date_of_transfer, amount) VALUES (?, ?, ?, ?)";
            PreparedStatement transferStmt = connection.prepareStatement(transferQuery);
            transferStmt.setInt(1, from_account);
            transferStmt.setInt(2, to_account);
            transferStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            transferStmt.setFloat(4, amount);
            transferStmt.executeUpdate();

            // Commit transaction
            connection.commit();
            System.out.println("Transfer successful.");
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback transaction if any error occurs
                System.out.println("Transfer failed. Rolling back transaction.");
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Reset auto-commit mode
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
