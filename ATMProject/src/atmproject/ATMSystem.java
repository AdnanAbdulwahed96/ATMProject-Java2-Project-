package atmproject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ATMSystem {

    private ArrayList<UserAccount> Users;

    public ArrayList<UserAccount> getUsers() {
        return Users;
    }

    ATMSystem() {
        this.Users = FileHandler.usersPrinter();
    }

    public void Run() {
        Scanner input = new Scanner(System.in);
        int operationNum;
        do {
            try {
                System.out.println("********************************");
                System.out.println("Welcome to the ATM!  \n"
                        + "Please choose one of the following options:  \n"
                        + "1. Log in  \n"
                        + "2. Create an account \n"
                        + "3. Technical Support \n"
                        + "4. Exit "
                );
                System.out.println("********************************");
                operationNum = input.nextInt();
                switch (operationNum) {
                    case 1:
                        login(input);
                        break;
                    case 2:
                        newAcount(input);
                        break;
                    case 3:
                        technicalSupport(input);
                        break;
                    case 4:
                        try {
                            FileHandler.usersWriter(Users);
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error reading file: " + ex.getMessage());
                        }
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid entry (enter number only)");
                input.next();
            }
        } while (true);
    }

    public void login(Scanner input) {

        System.out.print("Enter Account Number: ");
        String accountNumber = input.next();

        System.out.print("Enter Password: ");
        String password = input.next();

        if (!this.Users.isEmpty()) {
            boolean accountFound = false;
            for (UserAccount user : Users) {
                if (user.getAccountNumber().equals(accountNumber)) {
                    accountFound = true;
                    if (user.getCounter() < 3) {
                        if (user.verifyPassword(password)) {
                            System.out.println("Login successful!");
                            performTransactions(input, user);
                        } else {
                            System.out.println("Incorrect password. Please try again.");
                            if (user.getCounter() == 3) {
                                try {
                                    FileHandler.usersWriter(Users);
                                } catch (FileNotFoundException ex) {
                                    System.out.println("Error reading file: " + ex.getMessage());
                                }
                            }
                        }
                    } else {
                        System.out.println("Account locked after 3 incorrect attempts. Contact support to unlock.");

                    }
                    break;
                }
            }
            if (!accountFound) {
                System.out.println("Invalid account number. Please check and try again");
            }

        } else {
            System.out.println("Invalid credentials.");
        }

    }

    public void newAcount(Scanner input) {

        boolean X = false;

        System.out.print("Enter Full Name: ");
        input.nextLine();
        String fullName = input.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = input.next();

        System.out.print("Set Password: ");
        input.nextLine();
        String password = input.nextLine();
        while (!X) {
            if (UserAccount.checkNumberOfPass(password)) {
                UserAccount newUser = new UserAccount(UserAccount.randomAccountNumber(), fullName, phoneNumber, password, 0, "Active");
                Users.add(newUser);
                X = true;
                System.out.println("Account created successfully! Your Account Number is: " + newUser.getAccountNumber());
                try {
                    FileHandler.usersWriter(Users);
                } catch (FileNotFoundException ex) {
                    System.out.println("Error reading file: " + ex.getMessage());
                }
            } else {
                System.out.println("Error! Reset password (password must be more than 8 characters): ");
                password = input.nextLine();

            }
        }
    }

    public void performTransactions(Scanner input, UserAccount user) {
        int operationNum;
        do {
            try {
                System.out.println("********************************");
                System.out.println("Hello, and welcome to ATM! Your account number is: " + user.getAccountNumber() + "\n"
                        + "Please choose one of the following options:  \n"
                        + "1. Deposit.\n"
                        + "2. Withdraw.\n"
                        + "3. Check Balance.\n"
                        + "4. View Transaction History.\n"
                        + "5. Change Password. \n"
                        + "6. Logout."
                );
                System.out.println("********************************");
                operationNum = input.nextInt();

                switch (operationNum) {
                    case 1:
                        System.out.print("Enter Amount to Deposit: ");
                        int depositAmount = input.nextInt();
                        user.deposit(depositAmount);
                        try {
                            FileHandler.usersWriter(Users);
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error reading file: " + ex.getMessage());
                        }
                        break;
                    case 2:
                        System.out.print("Enter Amount to Withdraw: ");
                        int withdrawAmount = input.nextInt();
                        user.withdraw(withdrawAmount);
                        try {
                            FileHandler.usersWriter(Users);
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error reading file: " + ex.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println("Your current account balance is " + user.checkBalance() + "$");
                        break;
                    case 4:
                        viewTransactionHistory(input, user);
                        break;
                    case 5:
                        channgePassword(input, user);
                        break;
                    case 6:
                        System.out.println("Logged out.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid entry (enter number only)");
                input.next();
            }

        } while (true);
    }

    public void viewTransactionHistory(Scanner input, UserAccount user) {
        int operationNum;
        do {
            try {
                System.out.println("********************************");
                System.out.println("Choose one of the options:  \n"
                        + "1. Print all Transaction History  \n"
                        + "2. Print Transaction History by Date  \n"
                        + "3. Create a file for Transaction History \n"
                        + "4. Back ");
                System.out.println("********************************");
                operationNum = input.nextInt();
                switch (operationNum) {
                    case 1:
                        String date = "null";
                        System.out.println("\t\t ||Transaction History:||  \t\t");
                        for (String transaction : user.viewTransactionHistory(date)) {
                            System.out.println(transaction);
                        }
                        break;
                    case 2:
                        System.out.print("Enter the date in the format (dd/mmm/year) Ex: (10/Jan/2025) If you need: ");
                        date = input.next();
                        System.out.println("\t\t ||Transaction History:||  \t\t");
                        for (String transaction : user.viewTransactionHistory(date)) {
                            System.out.println(transaction);
                        }
                        break;
                    case 3:
                        date = "null";
                        try {
                            FileHandler.transactionsWriterForUser(user.viewTransactionHistory(date), user);
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error reading file: " + ex.getMessage());
                        }
                        break;
                    case 4:
                        return;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid entry (enter number only)");
                input.next();
            }

        } while (true);
    }

    private void technicalSupport(Scanner input) {
        int operationNum;
        do {
            try {
                System.out.println("********************************");
                System.out.println("Welcome to the ATM Support!  \n"
                        + "Please choose one of the following options:  \n"
                        + "1. Unlock Account  \n"
                        + "2. Forgot Password \n"
                        + "3. Find User \n"
                        + "4. Back "
                );
                System.out.println("********************************");
                operationNum = input.nextInt();
                switch (operationNum) {
                    case 1:
                        unlockAccount(input);
                        break;
                    case 2:
                        forgotPassword(input);
                        break;
                    case 3:
                        findUser(input);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid entry (enter number only)");
                input.next();
            }
        } while (true);
    }

    private void unlockAccount(Scanner input) {
        System.out.print("Enter Account Number: ");
        String accountNumber = input.next();

        System.out.print("Enter Password: ");
        String password = input.next();

        System.out.print("Last Transaction Amount (only the number): ");
        String amount = input.next();

        if (!this.Users.isEmpty()) {
            boolean accountFound = false;
            for (UserAccount user : Users) {
                if (user.getAccountNumber().equals(accountNumber)) {
                    accountFound = true;
                    if (user.viewTransactionHistory("null").isEmpty()) {
                        if (user.unlockAccount(password)) {
                            System.out.println("Account Unlocked Successfully");
                            try {
                                FileHandler.usersWriter(Users);
                            } catch (FileNotFoundException ex) {
                                System.out.println("Error reading file: " + ex.getMessage());
                            }

                        } else {
                            System.out.println("Incorrect password. Please try again.**");
                        }
                    } else if ((user.viewTransactionHistory("null").get((user.viewTransactionHistory("null").size() - 1))).contains(amount + "$")) {
                        if (user.unlockAccount(password)) {
                            System.out.println("Account Unlocked Successfully");
                            try {
                                FileHandler.usersWriter(Users);
                            } catch (FileNotFoundException ex) {
                                System.out.println("Error reading file: " + ex.getMessage());
                            }
                        } else {
                            System.out.println("Incorrect password. Please try again.&&");
                        }
                    } else {
                        System.out.println("Amount of last transaction is incorrect");
                    }
                }
            }
            if (!accountFound) {
                System.out.println("Invalid account number. Please check and try again");
            }
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void forgotPassword(Scanner input) {
        System.out.print("Enter Account Number: ");
        String accountNumber = input.next();
        System.out.print("Enter Full Name: ");
        input.nextLine();
        String fullName = input.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = input.next();
        System.out.print("Last Transaction Amount (only the number): ");
        String amount = input.next();

        if (!this.Users.isEmpty()) {
            boolean accountFound = false;
            for (UserAccount user : Users) {
                if (accountNumber.equals(user.getAccountNumber())) {
                    accountFound = true;
                    if (user.getFullName().compareToIgnoreCase(fullName) == 0 && user.getPhoneNumber().compareToIgnoreCase(phoneNumber) == 0) {
                        if (user.viewTransactionHistory("null").isEmpty()) {
                            System.out.print("Set Password: ");
                            input.nextLine();
                            String password = input.nextLine();
                            boolean X = false;
                            while (!X) {
                                if (UserAccount.checkNumberOfPass(password)) {
                                    X = true;
                                    if (user.changePasseord(password)) {
                                        System.out.println("Password changed successfully");
                                        try {
                                            FileHandler.usersWriter(Users);
                                        } catch (FileNotFoundException ex) {
                                            System.out.println("Error reading file: " + ex.getMessage());
                                        }
                                    } else {
                                        System.out.println("The new password must be different from the old one.");
                                    }
                                } else {
                                    System.out.println("Error! Reset password (password must be more than 8 characters): ");
                                    password = input.nextLine();
                                }
                            }
                        } else if ((user.viewTransactionHistory("null").get((user.viewTransactionHistory("null").size() - 1))).contains(amount + "$")) {
                            System.out.print("Set Password: ");
                            input.nextLine();
                            String password = input.nextLine();
                            boolean X = false;
                            while (!X) {
                                if (UserAccount.checkNumberOfPass(password)) {
                                    X = true;
                                    if (user.changePasseord(password)) {
                                        System.out.println("Password changed successfully");
                                        try {
                                            FileHandler.usersWriter(Users);
                                        } catch (FileNotFoundException ex) {
                                            System.out.println("Error reading file: " + ex.getMessage());
                                        }
                                    } else {
                                        System.out.println("The new password must be different from the old one.");
                                    }
                                } else {
                                    System.out.println("Error! Reset password (password must be more than 8 characters): ");
                                    password = input.nextLine();
                                }
                            }
                        } else {
                            System.out.println("Amount of last transaction is incorrect");
                        }
                    } else {
                        System.out.println("The full name or phone number is incorrect.");
                    }
                }
            }
            if (!accountFound) {
                System.out.println("Invalid account number. Please check and try again");
            }
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void findUser(Scanner input) {
        System.out.print("Enter Account Number: ");
        String accountNumber = input.next();

        if (!this.Users.isEmpty()) {
            boolean accountFound = false;
            for (UserAccount user : Users) {
                if (user.getAccountNumber().equals(accountNumber)) {
                    accountFound = true;
                    System.out.println("Account found: Name: " + user.getFullName() + ", PhoneNumber: " + user.getPhoneNumber() + ", Balance: " + user.checkBalance());
                }
            }
            if (!accountFound) {
                System.out.println("Account with account number " + accountNumber + " not found");
            }
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void channgePassword(Scanner input, UserAccount user) {
        System.out.print("Enter the old password: ");
        input.nextLine();
        String oldpassword = input.nextLine();
        if (user.verifyPassword(oldpassword)) {
            System.out.print("Enter the new password: ");
            String Newpassword = input.nextLine();
            boolean X = false;
            while (!X) {
                if (UserAccount.checkNumberOfPass(Newpassword)) {
                    X = true;
                    if (user.changePasseord(Newpassword)) {
                        System.out.println("Password changed successfully");
                        try {
                            FileHandler.usersWriter(Users);
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error reading file: " + ex.getMessage());
                        }
                    } else {
                        System.out.println("The new password must be different from the old one.");
                    }
                } else {
                    System.out.println("Error! Reset password (password must be more than 8 characters): ");
                    Newpassword = input.nextLine();
                }
            }
        } else {
            System.out.println("The old password is incorrect");
        }
    }

}
