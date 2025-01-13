
package atmproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class FileHandler {
    private final static File usersData  = new File("usersData.txt");
    private final static File transactions  = new File("transactions.txt");

    public static File getUsersData() {
        return usersData;
    }

    public static File getTransactions() {
        return transactions;
    }
    
    
    
    static void usersWriter(ArrayList<UserAccount> A) throws FileNotFoundException{
        try(java.io.PrintWriter output = new java.io.PrintWriter(usersData);){
            for (UserAccount user: A ) {
                output.println(user.toString());}
        }  
    }
    
    static void transactionsWriter(ArrayList<String> transactionHistory) throws FileNotFoundException{
        try(java.io.PrintWriter output = new java.io.PrintWriter(transactions);){
            for (String transaction: transactionHistory ) {
                output.println(transaction);}
        }    
    }
    
    static ArrayList<String> transactionsPrinter(){
        ArrayList<String> transactions1 = new ArrayList<>();
        if(transactions.exists()){
            try(Scanner input = new Scanner(transactions);){
                while (input.hasNext()){
                    transactions1.add(input.nextLine());
                }
            }catch (FileNotFoundException ex) {
                System.out.println("Error reading file: " + ex.getMessage());
            }
        }else {transactions1 = new ArrayList<>();}
        
        return transactions1;
    }
    
    
    
    static ArrayList<UserAccount> usersPrinter(){
        
        ArrayList<String> fileData = new ArrayList<>();
        ArrayList<UserAccount> users = new ArrayList<>();
        if(usersData.exists()){
            try(Scanner input = new Scanner(usersData);){
                while (input.hasNext()){
                    fileData.add(input.nextLine());
                }
                for (String user: fileData ) {
                    String[] A = user.split(",");
                    UserAccount User = new UserAccount(A[0],A[1],A[2],A[3],Double.parseDouble(A[4]),A[5]);
                    users.add(User);
                    }
            }catch (FileNotFoundException ex) {
                System.out.println("Error reading file: " + ex.getMessage());
            }
        }else {users = new ArrayList<>();}
        
        return users;
    }
    
    static void transactionsWriterForUser(ArrayList<String> A,UserAccount user) throws FileNotFoundException{
        File ABC  = new File(String.format("(%s transactions).txt",user.getAccountNumber()));
        try(java.io.PrintWriter output = new java.io.PrintWriter(ABC);){
            for (String B: A ) {
                output.println(B);}
            System.out.println("Done!");
        }  
    }

   
    
    
    

   
    
    
}
