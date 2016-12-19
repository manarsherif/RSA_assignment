package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Bank {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Statement stmt = null;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system","root","1234");
				stmt=(Statement) con.createStatement();
				//System.out.println("connection succeded");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			System.out.println("Please choose one of the following operations");
			System.out.println("1. Do a transaction");
			System.out.println("2. Add a new account");
			System.out.println("3. Review a customer's account");
			System.out.println("4. get a manager's contact");
			System.out.println("5. exit");
			Scanner scanner = new Scanner(System.in);
			String input= scanner.nextLine();
			switch (input)
			{
			case "1"://transaction
				System.out.println("please enter the transaction Type");
				System.out.println("1. Withdraw");
				System.out.println("2. Deposit");
				Transactions trans=new Transactions(stmt,con);
				String input2=scanner.nextLine();
				switch (input2)
				{
				case "1"://withdraw
					trans.withdraw();
					break;
				case "2"://deposit
					trans.deposit();
					break;
				}
				break;
			case "2"://add a new account
				AccountManager ac=new AccountManager(stmt, con);
				ac.addNewAccount("0","2");
				break;
			case "5":
				break;
			}
			scanner.close();
		}
			
		

}
