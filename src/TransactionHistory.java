import java.sql.*;

public class TransactionHistory {

    private int id_transaction;
   private int id_account;
   private  float amount;
   private TransactionType type;


public TransactionHistory() {}

    public void viewTransactionHistory(Connection connection,int id_customer) {

        String query = "SELECT ht.id_account, ht.transaction_type, ht.amount, ht.transaction_date " +
                "FROM transaction_history ht " +
                "JOIN account a ON ht.id_account = a.id_account " +  // Added space before "JOIN"
                "WHERE a.id_customer = ? ORDER BY ht.transaction_date DESC"; // Added space before "WHERE"


        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id_customer);

            ResultSet resultSet = stmt.executeQuery();

            System.out.println("Your Transactions:");
            System.out.println("----------------------------------------");
            System.out.printf("%-15s %-15s %-10s %-20s\n", "Account ID", "Type", "Amount", "Date");
            System.out.println("----------------------------------------");

            while (resultSet.next()) {
                int id_account = resultSet.getInt("id_account");
                String transaction_type = resultSet.getString("transaction_type");
                float amount = resultSet.getFloat("amount");
                Timestamp transaction_date = resultSet.getTimestamp("transaction_date");

                System.out.printf("%-15d %-15s %-10.2f %-20s\n", id_account, transaction_type, amount, transaction_date.toString());
            }
            System.out.println("---------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving accounts.");
            e.printStackTrace();
        }
}
    public boolean addTransaction(Connection connection, TransactionType type, int id_account, float  amount) {


        try {
            String query="INSERT INTO transaction_history (id_account,transaction_type,amount,transaction_date) VALUES (?,?,?, NOW())";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, id_account);
            preparedStatement.setInt(2, type.ordinal());
            preparedStatement.setFloat(3, amount);
            int rowsInserted= preparedStatement.executeUpdate();



            if (rowsInserted > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.id_transaction = generatedKeys.getInt(1);
                }
                return true;
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }


}
