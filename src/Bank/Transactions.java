package Bank;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.jdbc.Statement;

public class Transactions {
	private Scanner scanner ;
	private Statement stmt;
	private Connection con;
	
	Transactions(Statement stmt,Connection con)
	{
		this.scanner=new Scanner(System.in);
		this.stmt=stmt;
		this.con=con;
	}
	
	public void withdraw(float amount,String account_number ) throws SQLException
	{
		
		String query ="SELECT AccBalance FROM Account WHERE AccNumber="+account_number+";";
		ResultSet rs=stmt.executeQuery(query);
		if(rs.next())
		{
			float balance =Float.parseFloat(rs.getString("AccBalance"));
			if(amount>balance)
				System.out.println("You don't have enough money in your accont");
			else
			{
				balance=balance-amount;
				query="UPDATE Account SET AccBalance="+Float.toString(balance)+"WHERE AccNumber="+account_number+";";
				stmt.execute(query);
				System.out.println("Withdrawal succeded");
				query ="SELECT AccBalance FROM Account WHERE AccNumber="+account_number+";";
				//System.out.println(query);
				rs=stmt.executeQuery(query);
				if(rs.next())
				{
					balance=Float.parseFloat(rs.getString("AccBalance"));
					System.out.println("your current balance is "+balance);
				}
			}
		}
		else
		{
			System.out.println("You entered a non exisiting account");
			System.out.println("Abort");
		}	
	}
	public void deposit(float amount1,String account_number1) throws SQLException
	{
		
		String query1 ="SELECT AccBalance FROM Account WHERE AccNumber="+account_number1+";";
		ResultSet rs1=stmt.executeQuery(query1);
		if(rs1.next())
		{
			float balance =Float.parseFloat(rs1.getString("AccBalance"));
			balance=balance+amount1;
			query1="UPDATE Account SET AccBalance="+Float.toString(balance)+"WHERE AccNumber="+account_number1+";";
			stmt.execute(query1);
			query1 ="SELECT AccBalance FROM Account WHERE AccNumber="+account_number1+";";
			rs1=stmt.executeQuery(query1);
			if(rs1.next())
			{
				balance=Float.parseFloat(rs1.getString("AccBalance"));
				System.out.println("your current balance is "+balance);
			}
			System.out.println("Deposit succeded");	
			
		}
		else
		{
			System.out.println("You entered a non exisiting account");
			System.out.println("Abort");
		}
		
	}
	
}