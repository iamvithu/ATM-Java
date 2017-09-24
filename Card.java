package edu.oop.atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

public class Card {
	private int cardno;
	private int pin;
	private int cvv = 123;
	private String dateFrom;
	private String dateThru;
	private String[] accountNumbers= new String[10];
	
	LinkedList<Accounts> accountList = new LinkedList<Accounts>();
	
	public void setCardNo(int id){
		cardno = id;
	}
	
	public void setPin(int pass){
		pin = pass;
	}
	
	public int getCardNo(){
		return cardno;
	}
	
	public int getPin(){
		return pin;
	}
	
	public boolean checkCardValidity(){
		boolean state= false;
		String s = null;
		int tempNo= 0;
		int tempPin = 0;
		try{
			Class.forName("com.mysql.jdbc.Driver");

			Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false","root","root");
			//here bank is the database name, root is the user name and root is the password
			Statement stmt=con.createStatement();

			ResultSet rs=stmt.executeQuery("SELECT cardNumber,pin FROM card WHERE cardNumber = "+cardno);

			
			while(rs.next()){
				tempNo = rs.getInt("cardNumber");
		        tempPin = rs.getInt("pin");
			}
			
			if (cardno == tempNo && pin == tempPin){
				state = true;
			}
			
			con.close();
		}
		catch(Exception e){ 
			System.out.println(e);
		}
		//return s;
		return state;
	}
	
	public void setAccountDetails(){
		int tempNo = 0;
		if(checkCardValidity() == true){
			
			try{
				Class.forName("com.mysql.jdbc.Driver");

				Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false","root","root");
				//here bank is the database name, root is the user name and root is the password
				Statement stmt=con.createStatement();

				ResultSet rs=stmt.executeQuery("SELECT account_no FROM accounts WHERE cardNumber = "+cardno);

				
				while(rs.next()){
					Accounts ac = new Accounts(rs.getInt("account_no"));
					accountList.add(ac);
				}
				
				con.close();
			}
			catch(Exception e){ 
				System.out.println(e);
			}
			//accountList.add();
		}
	}
	public Accounts getAccount(int no){
		
		return accountList.get(no-1);
	}
	
	public LinkedList<Accounts> getAccList(){
		return accountList;
	}

}
