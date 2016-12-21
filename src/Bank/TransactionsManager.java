package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import extras.Constants;
import extras.Globals;

public class TransactionsManager {
	private static TransactionsManager transactionsManagerInstance = null;
	private Statement stmt;
	private Connection conn;

	private TransactionsManager() {
		try {
			Class.forName(Constants.JDBC_DRIVER);
			conn = (Connection) DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASS);
			stmt = (Statement) 	conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static TransactionsManager getTransactionsManagerInstance() {
		if(transactionsManagerInstance == null)
		{
			transactionsManagerInstance = new TransactionsManager();
		}
		return transactionsManagerInstance;
	}
	
	public float withdraw(float amount,String account_number ) throws SQLException
	{
		
		String query ="SELECT AccBalance FROM Account WHERE AccNumber="+account_number+";";
		ResultSet rs=stmt.executeQuery(query);
		float balance;
		Boolean flag=false;
		if(rs.next())
		{
			balance =Float.parseFloat(rs.getString("AccBalance"));
			if(amount<balance)
			{
				flag=true;
				balance=balance-amount;
				query="UPDATE Account SET AccBalance="+Float.toString(balance)+"WHERE AccNumber="+account_number+";";
				stmt.execute(query);
				System.out.println("Withdrawal succeded");
				query ="SELECT AccBalance FROM Account WHERE AccNumber="+account_number+";";
				//System.out.println(query);
				rs=stmt.executeQuery(query);
				//add_transaction(account_number, amount, "Withdraw", Globals.SessionESSN);
				if(rs.next())
				{
					balance=Float.parseFloat(rs.getString("AccBalance"));
				}
			}
		}
		else
		{
			// wrong account num
			return -1;
		}	
		if(flag)
		{
			add_transaction(account_number, amount,"Withdraw", Globals.SessionESSN);
			return balance;
		}
		else 
			return -2;
	}
	public float deposit(float amount1,String account_number1) throws SQLException
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
			}
			System.out.println("Deposit succeded");	
			add_transaction(account_number1, amount1,"Deposit", Globals.SessionESSN);
			return balance;
			
		}
		else
		{
			// wrong account num
			return -1;
		}
		
	}
	public void add_transaction(String account_number,float amount,String TransType,String tellerSSN) throws SQLException
	{
		int TransactionId=(int)(Math.random()*999999);
		//String date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
		String query="INSERT INTO Transactions (TransactionID, AccountNum, TransAmount, TransType,  TellerSSN) VALUES	("+
				Integer.toString(TransactionId)+","+account_number+","+amount+",'"+
				TransType+"','"+tellerSSN+"');";
		System.out.println(query);
		stmt.execute(query);
	}
	
}
