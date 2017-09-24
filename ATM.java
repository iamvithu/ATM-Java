package edu.oop.atm;

import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import com.mysql.jdbc.PreparedStatement;

public class ATM {
	
	static Card insertedCard = new Card(); 
	static Accounts selectedAccount = new Accounts();
	
	public static void main(String[] args){
		inputFromuser();
		
	}
	public static void inputFromuser(){
		
		System.out.println("----------------Welcome to Bank-------------------");
		System.out.print("Enter your card number: ");
		Scanner scanner = new Scanner(System.in);
		String cardNo = scanner.nextLine();
		System.out.print("Enter your pin code: ");
		String pin = scanner.nextLine();
		
		insertedCard.setCardNo(Integer.parseInt(cardNo));
		insertedCard.setPin(Integer.parseInt(pin));
		if(insertedCard.checkCardValidity()){
			System.out.println("Login Successfully.");
			System();
		}
		else
			System.out.println("Login Failled!");
	}
	
	public static void System(){
		
		insertedCard.setAccountDetails();
		System.out.println("Select an account for action!");
		LinkedList <Accounts> tempAccList = new LinkedList<Accounts>();
		tempAccList = insertedCard.getAccList();
		//System.out.println("list count :"+ tempAccList.size());
		for(int x= 0;x< tempAccList.size() ;x++){
			Accounts ac = new Accounts();
			ac = tempAccList.get(x);
			System.out.println(x+1 +". "+ ac.accountNumber);
		}
		
		Scanner scanner = new Scanner(System.in);
		
		int selectedAccNo = scanner.nextInt();
		
		
		
		selectedAccount = insertedCard.getAccount(selectedAccNo);
		System.out.println("selected account number :"+ selectedAccount.accountNumber);
		
		System.out.println("Choose Action \n 1. Check Balance \n 2. Deposit Money \n 3. Withdraw Money");
		System.out.print("select by enter the action number : ");
		Scanner scanner3 = new Scanner(System.in);
		int option = scanner3.nextInt();
		
		switch(option){
		case 1 :
			System.out.println("Your current balance is "+ selectedAccount.getCurrentBalance());
			break;
		case 2 :
			Scanner scanner4 = new Scanner(System.in);
			System.out.println("Enter deposit amount : ");
			int damt = scanner4.nextInt();
			selectedAccount.setAmount(damt, 'D');
			break;
		case 3 :
			Scanner scanner2 = new Scanner(System.in);
			System.out.println("Enter withdrawal amount : ");
			int wamt = scanner2.nextInt();
			selectedAccount.setAmount(wamt, 'W');
			break;
		}
		/*
		System.out.println("Enter ammount : ");
		Accounts act= new Accounts(1201245);
		System.out.println("Your current balance is "+ act.getCurrentBalance());
		
		Scanner scanner2 = new Scanner(System.in);
		System.out.println("Enter ammount : ");
		String amt = scanner2.nextLine();
		
		act.setAmount(Integer.parseInt(amt));
		System.out.println("Recieved ammount is "+ act.getAmount());
		act.deposit();
		
		Scanner scanner4 = new Scanner(System.in);
		System.out.println("Enter ammount : ");
		String opt = scanner4.nextLine();  */
		
	}
	
	

}
class Accounts{
	int accountNumber;
	int currentBalance;
	int wAmount;
	int dAmount;
	int finalAmount=0;
	int amount=0;
	
	
	LinkedList<String> accountList = new LinkedList<String>();
	//ArrayList<String> accountList;
	
	public Accounts(){
		
	}
	
	public Accounts(int actN){
		 //accountList = new ArrayList<String>();
		accountNumber = actN;
		currentBalance();
		
	}
	
	public void currentBalance(){
		int tempBalance=0;
		try{
			Class.forName("com.mysql.jdbc.Driver");

			Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false","root","root");
			//here bank is the database name, root is the user name and root is the password
			Statement stmt=con.createStatement();

			ResultSet rs=stmt.executeQuery("SELECT balance FROM accounts WHERE account_no = "+accountNumber);
			
			while(rs.next()){
				tempBalance = Integer.parseInt(rs.getString("balance"));
			}
			
			currentBalance = tempBalance;
			con.close();
		}
		catch(Exception e){ 
			System.out.println(e);
		}
	}
	public int getCurrentBalance(){
		return currentBalance;
	}
	public int getAccNo(){
		return accountNumber;
	}
	public void updateBalance(int amt){
		try
	    {
	      // create a java mysql database connection
	      Class.forName("com.mysql.jdbc.Driver");
	      Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false","root","root");
	    
	      // create the java mysql update preparedstatement
	      String query = "update accounts set balance = ? where account_no = ?";
	      PreparedStatement preparedStmt = (PreparedStatement) conn.prepareStatement(query);
	      preparedStmt.setInt   (1, amt);
	      preparedStmt.setInt(2, accountNumber);

	      // execute the java preparedstatement
	      preparedStmt.executeUpdate();
	      
	      conn.close();
	      System.out.println("Updated Successfully!");
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      System.err.println(e.getMessage());
	    }
	}
	public void setAmount(int amt,char type){
		switch(type){
		case 'W':
			wAmount = amt;
			withdraw();
			break;
		case 'D':
			dAmount = amt;
			deposit();
			break;
		}
	}
	public int getAmount(){
		return finalAmount;
	}
	public void deposit(){
		finalAmount = dAmount + currentBalance;
		updateBalance(finalAmount);
	}
	public void withdraw(){
		finalAmount = currentBalance - wAmount;
		updateBalance(finalAmount);
	}
}
