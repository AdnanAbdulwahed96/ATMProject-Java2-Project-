/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atmproject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UserAccount {

    private String accountNumber;
    private String fullName;
    private String phoneNumber;
    private String password;
    private String AccountStatus;
    private double balance;
    private static ArrayList<String> transactionHistory = FileHandler.transactionsPrinter();
    private int counter;

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getCounter() {
        return counter;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserAccount(String accountNumber, String fullName, String phoneNumber, String password, double balance, String accountStatus) {
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.balance = balance;
        this.AccountStatus = accountStatus;
    }

    @Override
    public String toString() {
        return accountNumber + "," + fullName + "," + phoneNumber + "," + password + "," + balance + "," + AccountStatus;
    }

    static boolean checkNumberOfPass(String pass) {
        return (pass.length() >= 8);
    }

    public boolean verifyPassword(String password) {
        if (this.password.equals(password) && counter < 3) {
            counter = 0;
            return true;
        } else {
            counter++;
            if (counter >= 3) {
                this.AccountStatus = "Locked";
            }
            return false;
        }
    }

    public boolean changePasseord(String password) {
        if (!password.equals(this.password)) {
            this.password = password;
            return true;
        } else {
            return false;
        }

    }

    public boolean unlockAccount(String password) {
        if (password.equals(this.password)) {
            this.counter = 0;
            this.AccountStatus = "Active";
            return true;
        } else {
            return false;
        }
    }

    static String randomAccountNumber() {
        long timestamp = System.currentTimeMillis();
        String timestampString = String.valueOf(timestamp);
        return timestampString.substring(timestampString.length() - 7);
    }

    public void deposit(int amount) {
        java.util.Date date = new java.util.Date();
        if (amount > 0) {
            balance += amount;
            transactionHistory.add("Account Number: " + accountNumber + "  ||  " + amount + "$  ||  " + "deposit" + "  ||  " + date.toGMTString());
            try {
                FileHandler.transactionsWriter(transactionHistory);
            } catch (FileNotFoundException ex) {
                System.out.println("Error reading file: " + ex.getMessage());
            }
            System.out.println("An amount of " + amount + "$ has been deposited into your account. Your current balance is " + balance + "$");
        } else {
            System.out.println("An error has occurred. The entered value is incorrect. Please try again.");
        }
    }

    public void withdraw(int amount) {
        java.util.Date date = new java.util.Date();
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add("Account Number: " + accountNumber + "  ||  " + amount + "$  ||  " + "withdraw" + "  ||  " + date.toGMTString());
            try {
                FileHandler.transactionsWriter(transactionHistory);
            } catch (FileNotFoundException ex) {
                System.out.println("Error reading file: " + ex.getMessage());
            }
            System.out.println("An amount of " + amount + "$ has been withdrawn from your account. Your current balance is " + balance + "$");
        } else {
            System.out.println("The entered amount exceeds your current account balance. Please enter a valid amount and try again.");
        }
    }

    public double checkBalance() {
        return balance;
    }

    public ArrayList<String> viewTransactionHistory(String date) {
        ArrayList<String> transactionHistoryForUser = new ArrayList<>();
        if ("null".equals(date)) {
            for (String A : transactionHistory) {
                if (A.contains(accountNumber)) {
                    transactionHistoryForUser.add(A);
                }
            }
        } else {
            String[] date1 = date.split("/");
            String Date = date1[0] + " " + date1[1] + " " + date1[2];
            for (String A : transactionHistory) {
                if (A.contains(accountNumber) && A.toLowerCase().contains(Date.toLowerCase())) {
                    transactionHistoryForUser.add(A);
                }
            }
        }
        return transactionHistoryForUser;
    }

}
