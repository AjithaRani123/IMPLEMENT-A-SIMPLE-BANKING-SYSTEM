import java.io.*;
import java.util.*;

// Base class
class BankAccount implements Serializable {
    private String accountNumber;
    private String name;
    private double balance;

    public BankAccount(String accountNumber, String name, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("✅ Deposited: ₹" + amount);
        } else {
            System.out.println("❌ Invalid deposit amount!");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("✅ Withdrawn: ₹" + amount);
        } else {
            System.out.println("❌ Insufficient balance or invalid amount!");
        }
    }

    public void displayInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Balance: ₹" + balance);
    }
}

// Inherited class (shows inheritance)
class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount(String accountNumber, String name, double balance, double interestRate) {
        super(accountNumber, name, balance);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }
}

// Handles saving/loading data to file
class FileHandler {
    private static final String FILE_NAME = "accounts.dat";

    // Save all accounts to file
    public static void saveAccounts(List<BankAccount> accounts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    // Load all accounts from file
    @SuppressWarnings("unchecked")
    public static List<BankAccount> loadAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (List<BankAccount>) ois.readObject();
        } catch (FileNotFoundException e) {
            // first run, file doesn’t exist yet
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
        return accounts;
    }
}

// Main class
public class BankingSystem {
    private static List<BankAccount> accounts = FileHandler.loadAccounts();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== SIMPLE BANKING SYSTEM =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    checkBalance();
                    break;
                case 5:
                    FileHandler.saveAccounts(accounts);
                    System.out.println("💾 Data saved. Exiting...");
                    System.exit(0);
                default:
                    System.out.println("❌ Invalid choice. Try again!");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.next();
        System.out.print("Enter Name: ");
        String name = sc.next();
        System.out.print("Enter Initial Balance: ");
        double balance = sc.nextDouble();

        BankAccount acc = new SavingsAccount(accNo, name, balance, 2.5);
        accounts.add(acc);
        FileHandler.saveAccounts(accounts);
        System.out.println("🎉 Account Created Successfully!");
    }

    private static void deposit() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.next();
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            System.out.print("Enter Amount to Deposit: ");
            double amount = sc.nextDouble();
            acc.deposit(amount);
            FileHandler.saveAccounts(accounts);
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    private static void withdraw() {
       System.out.print("Enter Account Number: ");
        String accNo = sc.next();
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            System.out.print("Enter Amount to Withdraw: ");
            double amount = sc.nextDouble();
            acc.withdraw(amount);
            FileHandler.saveAccounts(accounts);
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    private static void checkBalance() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.next();
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            acc.displayInfo();
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    private static BankAccount findAccount(String accNo) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equals(accNo)) {
                return acc;
            }
        }
        return null;
    }
    
}