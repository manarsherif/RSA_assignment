package Bank;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.jdbc.Statement;

public class CustomerManager {
	private Scanner scanner ;
	private Statement stmt;
	private Connection con;
	
	public CustomerManager(Statement stmt,Connection con) {
		// TODO Auto-generated constructor stub
		this.scanner=new Scanner(System.in);
		this.stmt=stmt;
		this.con=con;
	}
	public void addNewCustomer(String cssn) throws SQLException
	{
		System.out.println("Please enter the customer's full name");
		String name=scanner.nextLine();
		System.out.println("Please enter the customer's phone number");
		String phone=scanner.nextLine();
		System.out.println("Please enter the customer's address");
		String address=scanner.nextLine();
		String query="INSERT INTO Customer (SSN, CNAME, CPhone, CAddress) VALUES ('"+cssn+"','"+ name+"', '"+phone+"', '"+address+"');";
		System.out.println("Welcome to our bank Mr/Mrs"+name);
		stmt.execute(query);
	}

}
