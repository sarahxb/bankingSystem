import java.sql.Connection;
import java.util.Scanner;

public class menus {
    public boolean logged;
    public boolean quit;

    private static Scanner scanner = new Scanner(System.in);

    public static void menuA() {
        System.out.println("Welcome! Choose an option: \n1. Sign Up \n2. Log In \n3. Quit");

    }

    public static void menuB() {
        System.out.println("Choose an option: \n1. View accounts \n2. Create account \n3.  Log out");
    }

    public static void menuC() {
        System.out.println("Choose an option: \n1. Deposit \n2. Withdrawal \n3. Transfer \n4. View account transactions \n5. Exit");
    }

    public void SetLogged(boolean logged) {
        this.logged = logged;
    }

    public void SetQuit(boolean quit) {
        this.quit = quit;
    }

    public void choiceA(Customer customer, Connection connection, Account account) {
        menuA();
        SetQuit(false);
        logged = false;

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {

            customer.signUpCustomer(connection, scanner);
            if (customer.loggingCustomer(connection, scanner))
            {SetLogged(true);
            choiceB(account, connection, customer);}
            else
                choiceA(customer, connection, account);

        } else if (choice == 2) {
            if (customer.loggingCustomer(connection, scanner))
            { SetLogged(true);
            choiceB(account, connection, customer);}
            else
                choiceA(customer, connection, account);
        } else if (choice == 3) {
            SetQuit(true);
            System.out.println("Thank you for using Bank System.Have a nice day!");

        } else {
            System.out.println("Invalid choice");
            choiceA(customer, connection, account);
        }
    }

    public void choiceB(Account account, Connection connection, Customer customer) {
        menuB();
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            account.viewAccounts(connection, customer.getId());
            choiceC(account, connection, customer);}
       else if (choice == 2) {
                account.createAccount(connection, customer.getId(), 0);
                choiceB(account, connection, customer);
            } else if (choice == 3) {
                System.out.println("Logging out..");
                choiceA(customer, connection, account);
            } else if (choice == 4) {
                System.out.println("Quitting the bank app.Thank you for using Bank System.Have a nice day!");
            } else {
                System.out.println("Invalid choice");
                choiceB(account, connection, customer);
            }

        }

        public void choiceC (Account account, Connection connection, Customer customer)
        {
            menuC();
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1) {
                account.viewCustomerAccounts(connection, customer.getId());
                System.out.println("Which account? :");
                int id = scanner.nextInt();
                System.out.println("Enter the sum: ");
                float amount = scanner.nextFloat();
                account.deposit(connection, id, amount);
                choiceC(account, connection, customer);
            } else if (choice == 2) {
                account.viewCustomerAccounts(connection, customer.getId());
                System.out.println("Which account? :");
                int id = scanner.nextInt();
                System.out.println("Enter the sum: ");
                float amount = scanner.nextFloat();
                amount = amount * -1;
                account.deposit(connection, id, amount);
                choiceC(account, connection, customer);

            } else if (choice == 3) {
                account.viewCustomerAccounts(connection, customer.getId());
                System.out.println("Enter the source account ID:");
                int sourceId = scanner.nextInt();
                System.out.println("Enter the destination account ID:");
                int destinationId = scanner.nextInt();
                System.out.println("Enter the amount to transfer:");
                float transferAmount = scanner.nextFloat();
                Transfer transfer = new Transfer();
                boolean success = transfer.executeTransfer(connection, sourceId, destinationId, transferAmount);
                if (!success)
                    System.out.println("Transfer could not be completed.");
                choiceC(account, connection, customer);

            } else if (choice == 4) {
                System.out.println("Transactions history");
                choiceC(account, connection, customer);
            } else if (choice == 5) {
                choiceB(account, connection, customer);
            } else {
                System.out.println("Invalid choice");
                choiceC(account, connection, customer);
            }

        }
    }




